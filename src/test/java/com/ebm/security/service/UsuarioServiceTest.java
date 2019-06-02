package com.ebm.security.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.geral.exceptions.DataIntegrityException;
import com.ebm.geral.service.PopulaBD;
import com.ebm.pessoal.service.CargoService;
import com.ebm.pessoal.service.FuncionarioService;
import com.ebm.pessoal.service.PessoaService;

@ActiveProfiles("testauto")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UsuarioServiceTest {

	@Autowired
	private UsuarioService userService;
	@Autowired
	private PessoaService pessoaService;
	@Autowired
	private FuncionarioService funcionarioService;
	@Autowired
	private CargoService cargoS;
	@Autowired
	private GrupoService grupoService;
	@Autowired
	private PopulaBD bd;

	@Before
	public void setUp() {

		bd.instanciaFuncionario(true).instanciaGrupo().instanciaUsuarios();
		bd.user1.setGrupo(bd.grup1);
	
		grupoService.save(bd.grup1);
		grupoService.save(bd.grup2);
		cargoS.save(bd.funf1.getCargo());
		cargoS.save(bd.funf2.getCargo());
		pessoaService.save(bd.funf1.getPessoa());
		pessoaService.save(bd.funf2.getPessoa());
		funcionarioService.save(bd.funf1);
		funcionarioService.save(bd.funf2);
	}
	
	@Transactional
	@Test
	public void testSave() {
		userService.save(bd.user1);
		
		assertNotNull(bd.user1.getId());
		assertThat(bd.user1.getId(), equalTo(bd.user1.getFuncionario().getId()));
		assertThat(bd.user1.getFuncionario().getId(), equalTo(bd.user1.getFuncionario().getPessoa().getId()));
		assertNotNull(bd.grup1.getId());
		assertThat(bd.user1.getGrupo().getId(), equalTo(bd.grup1.getId()));
	}
	
	
	@Test
	public void testTrocaFuncionarioDeveLancarEx() {
		bd.user1 = userService.save(bd.user1);
		bd.user1.setFuncionario(bd.funf2);
		try {
			userService.save(bd.user1);
			fail();
		}catch(DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(UsuarioService.DATAINTEGRITY_CHANGEFUNC));
		}
		
		userService.deleteById(bd.user1.getId());
		grupoService.deleteById(bd.grup1.getId());
		grupoService.deleteById(bd.grup2.getId());
		funcionarioService.deleteAll();
		cargoS.deleteAll();

		pessoaService.deleteAll(true);
	}
	
	@Transactional
	@Test
	public void testUpdateGrupo() {
		bd.user1 = userService.save(bd.user1);
		
		bd.user1.setGrupo(bd.grup2);
		
		bd.user1 = userService.save(bd.user1);
		
		assertThat(bd.user1.getGrupo().getNome(), equalTo(bd.grup2.getNome()));
	}
	
	public void prepara() {
		bd.user1 = userService.save(bd.user1);
		bd.user2 = userService.save(bd.user2);
	}
	
	@Transactional
	@Test
	public void testFindByWithGrupo() {
		prepara();
	}
	
	@Transactional
	@Test
	public void testFindByAllNull() {
		prepara();
	}
	

	@Transactional
	@Test
	public void testFindByWithLogin() {
		prepara();
	}
	
	@Transactional
	@Test
	public void testFindByWithEmail() {
		prepara();
	}

}
