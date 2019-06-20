package com.ebm.security.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.BaseTest;
import com.ebm.geral.service.PopulaBD;
import com.ebm.geral.utils.Utils;
import com.ebm.pessoal.domain.Funcionario;
import com.ebm.pessoal.service.CargoService;
import com.ebm.pessoal.service.FuncionarioService;
import com.ebm.pessoal.service.PessoaService;
import com.ebm.security.PermissaoE;
import com.ebm.security.dto.UsuarioListDTO;
import com.ebm.security.dto.UsuarioNewDTO;
import com.ebm.security.dto.UsuarioUpdateDTO;

public class UsuarioServiceTest  extends BaseTest{

	@Autowired
	private UsuarioService userService;
	@Autowired
	private PessoaService pessoaService;
	@Autowired
	private FuncionarioService funcionarioService;
	@Autowired
	private CargoService cargoS;
	@Autowired
	private PopulaBD bd;

	@Before
	public void setUp() {

		bd.instanciaFuncionario(true).instanciaUsuarios();
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
		bd.user1.setFuncionario(new Funcionario(bd.funf1.getId(), null, null, null, null, null, null));
		bd.user1 = userService.save(UsuarioNewDTO.fromUsuario(bd.user1));

		assertNotNull(bd.user1.getId());
		assertThat(bd.user1.getId(), equalTo(bd.user1.getFuncionario().getId()));
		assertThat(bd.user1.getFuncionario().getId(), equalTo(bd.user1.getFuncionario().getPessoa().getId()));
	}

	@Transactional
	@Test
	public void testUpdate() {
		bd.user1 = userService.save(UsuarioNewDTO.fromUsuario(bd.user1));
		UsuarioUpdateDTO userDTO = UsuarioUpdateDTO.fromUsuario(bd.user1);
		userDTO.setLogin("novologin");
		userDTO.setPermissoes(null);
		userDTO.setPermissoes(new HashSet<>(Arrays.asList(PermissaoE.CARGO_POST)));
		bd.user1 = userService.update(userDTO);
		
		assertThat(bd.user1.getLogin(), equalTo("novologin"));
		assertThat(bd.user1.getPermissoes().size(), equalTo(1));
		assertNotNull(bd.user1.getHistorico().getDataUltimaModificacao() );
	}
	

	public void prepara() {

		cargoS.save(bd.funf3.getCargo());
		cargoS.save(bd.funf4.getCargo());
		bd.funf4.getPessoa().getEmail().add(Utils.getRandomEmail(bd.funf4.getPessoa(), false));
		pessoaService.save(bd.funf3.getPessoa());
		pessoaService.save(bd.funf4.getPessoa());
		funcionarioService.save(bd.funf3);
		funcionarioService.save(bd.funf4);
	    userService.saveAll(Arrays.asList(bd.user1,bd.user2,bd.user3,bd.user4));
	}

	@Transactional
	@Test
	public void testFindByAllNull() {
		prepara();

		Page<UsuarioListDTO> list = userService.findBy(null, null, null, PageRequest.of(0, 5));

		testSePossuITodos(list);
	}
	
	@Transactional
	@Test
	public void testPermissaoUserAdm() {
		assertTrue(bd.user5.getPermissoes().containsAll(Arrays.asList(PermissaoE.values())));
	}

	private void testSePossuITodos(Page<UsuarioListDTO> list) {
		assertThat(list.getNumberOfElements(), equalTo(4));
		assertTrue(list.stream().anyMatch(u -> u.getNome().equals(bd.funf1.getPessoa().getNome())));
		assertTrue(list.stream().anyMatch(u -> u.getNome().equals(bd.funf2.getPessoa().getNome())));
		assertTrue(list.stream().anyMatch(u -> u.getNome().equals(bd.funf3.getPessoa().getNome())));
		assertTrue(list.stream().anyMatch(u -> u.getNome().equals(bd.funf4.getPessoa().getNome())));
	}

	@Transactional
	@Test
	public void testFindByWithLogin() {
		prepara();

		Page<UsuarioListDTO> list = userService.findBy(null, "login", null, PageRequest.of(0, 5));

		assertThat(list.getNumberOfElements(), equalTo(3));
		assertTrue(list.stream().anyMatch(u -> u.getNome().equals(bd.funf4.getPessoa().getNome())));
		assertTrue(list.stream().anyMatch(u -> u.getNome().equals(bd.funf2.getPessoa().getNome())));
		assertTrue(list.stream().anyMatch(u -> u.getNome().equals(bd.funf3.getPessoa().getNome())));

	}

	@Transactional
	@Test
	public void testFindByWithEmail() {
		prepara();

		String emailNPrincipal = bd.funf4.getPessoa().getEmail().stream().filter(e -> !e.isPrincipal()).findFirst()
				.get().getEmail();
		Page<UsuarioListDTO> all = userService.findBy(null, null, "example", PageRequest.of(0, 5));
		Page<UsuarioListDTO> only4 = userService.findBy(null, null, bd.funf4.getPessoa().getEmailPrincipal().getEmail(),
				PageRequest.of(0, 2));
		Page<UsuarioListDTO> esperaN = userService.findBy(null, null, emailNPrincipal, PageRequest.of(0, 4));

		this.testSePossuITodos(all);

		assertThat(only4.getNumberOfElements(), equalTo(1));
		assertTrue(only4.stream().allMatch(u -> u.getNome().equals(bd.funf4.getPessoa().getNome())));

		assertThat(esperaN.getNumberOfElements(), equalTo(0));
	}

}
