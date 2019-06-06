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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.geral.exceptions.DataIntegrityException;
import com.ebm.geral.service.PopulaBD;
import com.ebm.geral.utils.Utils;
import com.ebm.pessoal.service.CargoService;
import com.ebm.pessoal.service.FuncionarioService;
import com.ebm.pessoal.service.PessoaService;
import com.ebm.security.dto.UsuarioListDTO;

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
		userService.save(bd.user1);

		assertNotNull(bd.user1.getId());
		assertThat(bd.user1.getId(), equalTo(bd.user1.getFuncionario().getId()));
		assertThat(bd.user1.getFuncionario().getId(), equalTo(bd.user1.getFuncionario().getPessoa().getId()));
	}

	@Test
	public void testTrocaFuncionarioDeveLancarEx() {
		bd.user1 = userService.save(bd.user1);
		bd.user1.setFuncionario(bd.funf2);
		try {
			userService.save(bd.user1);
			fail();
		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(UsuarioService.DATAINTEGRITY_CHANGEFUNC));
		}

		userService.deleteById(bd.user1.getId());

		funcionarioService.deleteAll();
		cargoS.deleteAll();

		pessoaService.deleteAll(true);
	}

	public void prepara() {

		cargoS.save(bd.funf3.getCargo());
		cargoS.save(bd.funf4.getCargo());
		bd.funf4.getPessoa().getEmail().add(Utils.getRandomEmail(bd.funf4.getPessoa(), false));
		pessoaService.save(bd.funf3.getPessoa());
		pessoaService.save(bd.funf4.getPessoa());
		funcionarioService.save(bd.funf3);
		funcionarioService.save(bd.funf4);
		bd.user1 = userService.save(bd.user1);
		bd.user2 = userService.save(bd.user2);
		bd.user3 = userService.save(bd.user3);
		bd.user4 = userService.save(bd.user4);
	}

	@Transactional
	@Test
	public void testFindByAllNull() {
		prepara();

		Page<UsuarioListDTO> list = userService.findBy(null, null, null, PageRequest.of(0, 5));

		testSePossuITodos(list);
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
