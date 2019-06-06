package com.ebm.security.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
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
import com.ebm.security.Grupo;


@ActiveProfiles("testauto")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GrupoServiceTest {

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
	public void testInsercaoSemUsuarioPersistido() {
		Grupo save = grupoService.save(bd.grup1);
		
		assertThat(save.getUsuarios().size(), equalTo(0));
	}
	@Transactional
	@Test
	public void testInsercaoRemoveUsuario() {
		Grupo save = grupoService.save(bd.grup1);
		userService.save(bd.user1);
		
		bd.grup1.removeUsuario(bd.user1);
	    save = grupoService.save(bd.grup1);
		
		assertThat(save.getUsuarios().size(), equalTo(0));
		
		try {
			userService.save(bd.user1);
			fail();
		}catch(DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(UsuarioService.DATAINTEGRITY));
		}
	}
	
	@Transactional
	@Test
	public void testaSaveNormal() {
		Grupo save = grupoService.save(bd.grup1);
		userService.save(bd.user1);
		
		save = grupoService.find(save.getId());
		
		assertNotNull(bd.grup1.getId());
		assertNotNull(bd.user1.getId());
		assertTrue(save.getUsuarios().contains(bd.user1));
	}
	
}
