package com.ebm.pessoal.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.Arrays;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ebm.Utils;
import com.ebm.exceptions.DataIntegrityException;
import com.ebm.exceptions.ObjectNotFoundException;
import com.ebm.geral.service.PopulaBD;
import com.ebm.pessoal.domain.Funcionario;
import com.ebm.pessoal.domain.PessoaFisica;
import com.ebm.pessoal.domain.PessoaJuridica;
import com.ebm.pessoal.domain.TipoPessoa;
import com.ebm.pessoal.dtos.FuncionarioListDTO;

@ActiveProfiles("testauto")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class FuncionarioServiceTest {

	@Autowired
	private FuncionarioService funcionarioService;
	@Autowired
	private PessoaService pessoaService;
	@Autowired
	private CargoService cargoService;
	@Autowired
	private PopulaBD bd;

	@Before
	public void setUp() {
		bd.instanciaFuncionario(true);
		cargoService.save(bd.cAdministrador);
		cargoService.save(bd.cDesenvolvedor);
		pessoaService.save(bd.pf1);
		pessoaService.save(bd.pj1);
	}

	@Transactional
	@Test
	public void testInsercaoSemPessoaDevLancarException() {

		try {
			bd.funf1.setPessoa(null);
			bd.funf1 = funcionarioService.save(bd.funf1);
			fail("Falha tentando inserir cliente sem pessoa associada, era esperado Data Integration Exception");

		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(FuncionarioService.DATAINTEGRITY_EMPLOYEWITHOUTPERSON));
		}

	}

	@Transactional
	@Test
	public void testInsersaoComMatriculaReptida() {
		funcionarioService.save(bd.funf1);
		try {
			bd.funj1.setMatricula(bd.funf1.getMatricula());
			funcionarioService.save(bd.funj1);
			fail();
		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(FuncionarioService.DATAINTEGRITY_DUPLICATEMATRICULA));
		}
	}

	@Transactional
	@Test
	public void testUpdateComMatriculaReptida() {
		bd.funj1 = funcionarioService.save(bd.funj1);

		try {
			bd.funf1.setMatricula(bd.funj1.getMatricula());
			funcionarioService.save(bd.funf1);
			fail();
		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(FuncionarioService.DATAINTEGRITY_DUPLICATEMATRICULA));
		}
	}

	@Transactional
	@Test
	public void testInsercao() {

		funcionarioService.save(bd.funf1);

		assertNotNull(bd.funf1.getId());
		assertNotNull(bd.funf1.getPessoa().getId());
		assertThat(bd.funf1.getId(), equalTo(bd.funf1.getPessoa().getId()));
	}

	@Transactional
	@Test
	public void testInsercaoClienteComPessoaQueJaPertenceAOutroCliente() {
		bd.funf1.setPessoa(bd.funj1.getPessoa());
		bd.funf1 = funcionarioService.save(bd.funf1);

		try {
			bd.funj1 = funcionarioService.save(bd.funj1);
			fail("Falha insercao de cliente com pessoa que ja pertence a outro cliente, deve lançar DataIntegratyExcpetion");
		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(FuncionarioService.DATAINTEGRITY_DUPLICATEPERSON));
		}

	}

	@Transactional
	@Test
	public void testUpdateSemMudarPessoa() {
		bd.funf1 = funcionarioService.save(bd.funf1);

		bd.funf1.setCargo(bd.cAdministrador);

		bd.funf1.getPessoa().setNome("novonome");
		bd.funf1 = funcionarioService.save(bd.funf1);

		assertThat(bd.funf1.getCargo(), equalTo(bd.cAdministrador));
		assertThat(bd.funf1.getPessoa().getNome(), equalTo("novonome"));
	}

	@Transactional
	@Test
	public void testUpdateMudarPessoa() {
		bd.funf1 = funcionarioService.save(bd.funf1);
		bd.funf1.setPessoa(bd.pj1);
		try {
			bd.funf1 = funcionarioService.save(bd.funf1);
			fail();
		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(FuncionarioService.DATAINTEGRITY_CHANCEPERSON));
		}
	}

	@Transactional
	@Test
	public void testUpdateClienteComPessoaPertenceOutroCliente() {
		bd.funf1 = funcionarioService.save(bd.funf1);
		bd.funj1 = funcionarioService.save(bd.funj1);

		bd.funf1.setPessoa(bd.funj1.getPessoa());
		try {
			bd.funf1 = funcionarioService.save(bd.funf1);
			fail("Falha insercao de cliente com pessoa que ja pertence a outro cliente, deve lançar DataIntegratyExcpetion");
		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(FuncionarioService.DATAINTEGRITY_DUPLICATEPERSON));
		}
	}

	@Transactional
	@Test
	public void testFindCnpj() {
		funcionarioService.save(bd.funj1);

		Funcionario result = funcionarioService.findByCpfOrCnpj(((PessoaJuridica) bd.funj1.getPessoa()).getCnpj());

		assertNotNull(result.getId());
		assertThat(((PessoaJuridica) result.getPessoa()).getCnpj(),
				equalTo(((PessoaJuridica) bd.funj1.getPessoa()).getCnpj()));
	}

	@Transactional
	@Test
	public void testFindCPF() {
		funcionarioService.save(bd.funf1);

		Funcionario result = funcionarioService.findByCpfOrCnpj(((PessoaFisica) bd.funf1.getPessoa()).getCpf());

		assertNotNull(result.getId());
		assertThat(((PessoaFisica) result.getPessoa()).getCpf(), equalTo(((PessoaFisica) result.getPessoa()).getCpf()));
	}

	@Transactional
	@Test
	public void testFindCPFEx() {

		try {
			funcionarioService.findByCpfOrCnpj("05909561162");
			fail();
		} catch (ObjectNotFoundException ex) {
			assertThat(ex.getMessage(), equalTo(PessoaService.NOT_FOUND_DOCUMENT + "05909561162"));
		}
	}

	private void cenarioParaBuscaParamiterizada() {
		pessoaService.saveAll(Arrays.asList(bd.pf2, bd.pf3, bd.pf4, bd.pj2, bd.pj3, bd.pj4));
		funcionarioService
				.saveAll(Arrays.asList(bd.funf1, bd.funf2, bd.funf3, bd.funf4, bd.funj1, bd.funj2, bd.funj3, bd.funj4));

	}

	@Transactional
	@Test
	public void testaBuscaParamiterizadaPessoaFisica() {

		cenarioParaBuscaParamiterizada();

		// executa
		PageRequest pageRequest = PageRequest.of(0, 8);
		Page<FuncionarioListDTO> result = funcionarioService.findBy(TipoPessoa.PESSOA_FISICA, null, null, null,
				pageRequest);

		// verifica
		assertThat(result.getNumberOfElements(), equalTo(4));
		assertTrue(result.get().allMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_FISICA.getDescricao())));
		assertFalse(result.get().anyMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_JURIDICA.getDescricao())));
	}

	@Transactional
	@Test
	public void testaBuscaParamiterizadaPessoaJuridica() {

		cenarioParaBuscaParamiterizada();

		// executa
		PageRequest pageRequest = PageRequest.of(0, 5);
		Page<FuncionarioListDTO> result = funcionarioService.findBy(TipoPessoa.PESSOA_JURIDICA, null, null, null,
				pageRequest);

		// verifica
		assertThat(result.getNumberOfElements(), equalTo(4));
		assertTrue(result.get().allMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_JURIDICA.getDescricao())));
		assertFalse(result.get().anyMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_FISICA.getDescricao())));
	}

	@Transactional
	@Test
	public void testaBuscaParamiterizadaPFOuPJ() {

		cenarioParaBuscaParamiterizada();

		// executa
		PageRequest pageRequest = PageRequest.of(0, 8);
		Page<FuncionarioListDTO> result = funcionarioService.findBy(null, null, null, null, pageRequest);

		// verifica
		assertThat(result.getNumberOfElements(), equalTo(8));
		assertTrue(result.get().anyMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_FISICA.getDescricao())));
		assertTrue(result.get().anyMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_JURIDICA.getDescricao())));
	}

	@Transactional
	@Test
	public void testaBuscaParamiterizadaNome() {

		cenarioParaBuscaParamiterizada();

		// executa
		PageRequest pageRequest = PageRequest.of(0, 8);
		Page<FuncionarioListDTO> result = funcionarioService.findBy(null, null, "joao", null, pageRequest);

		// verifica
		assertThat(result.getNumberOfElements(), equalTo(3));
		assertTrue(result.get().filter(c -> c.getTipo().equals(TipoPessoa.PESSOA_FISICA.getDescricao())).count() == 2);
		assertTrue(result.get().anyMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_JURIDICA.getDescricao())));

	}

	@Transactional
	@Test
	public void testaBuscaParamiterizadaNomeAndTipo() {

		cenarioParaBuscaParamiterizada();

		// executa
		PageRequest pageRequest = PageRequest.of(0, 8);
		Page<FuncionarioListDTO> result = funcionarioService.findBy(TipoPessoa.PESSOA_FISICA, null, "joao", null,
				pageRequest);

		// verifica
		assertThat(result.getNumberOfElements(), equalTo(2));
		assertTrue(result.get().allMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_FISICA.getDescricao())));

	}

	@Transactional
	@Test
	public void testaBuscaParamiterizadaAllNull() {

		cenarioParaBuscaParamiterizada();

		// executa
		PageRequest pageRequest = PageRequest.of(0, 8);
		Page<FuncionarioListDTO> result = funcionarioService.findBy(null, null, null, null, pageRequest);

		// verifica
		assertThat(result.getNumberOfElements(), equalTo(8));
		assertTrue(result.get().anyMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_FISICA.getDescricao())));
		assertTrue(result.get().anyMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_JURIDICA.getDescricao())));
	}

	@Transactional
	@Test
	public void testaBuscaParamiterizadaCargo() {

		cenarioParaBuscaParamiterizada();

		// executa
		PageRequest pageRequest = PageRequest.of(0, 8);
		Page<FuncionarioListDTO> result = funcionarioService.findBy(null, bd.cAdministrador.getNomeCargo(), null, null,
				pageRequest);

		// verifica
		assertThat(result.getNumberOfElements(), equalTo(4));
		assertTrue(result.get().allMatch(f -> f.getCargo().equals(bd.cAdministrador.getNomeCargo())));
		assertFalse(result.get().anyMatch(f -> f.getCargo().equals(bd.cDesenvolvedor.getNomeCargo())));
	}

	@Transactional
	@Test
	public void testaBuscaParamiterizadaCargoAndTipoNothing() {

		cenarioParaBuscaParamiterizada();

		// executa
		PageRequest pageRequest = PageRequest.of(0, 8);
		Page<FuncionarioListDTO> result = funcionarioService.findBy(TipoPessoa.PESSOA_FISICA,
				bd.cAdministrador.getNomeCargo(), null, null, pageRequest);

		// verifica
		assertThat(result.getNumberOfElements(), equalTo(0));
	}

	@Transactional
	@Test
	public void testaBuscaParamiterizadaCargoAndTipo() {

		cenarioParaBuscaParamiterizada();
		PessoaJuridica pj = new PessoaJuridica(null, "Dev Me", "93811961000167", "DevME", null, null);
		pj.getEndereco().add(bd.endereco1);
		pj.getTelefone().add(Utils.getRandomTelefone(true));
		pj.getEmail().add(Utils.getRandomEmail(pj, true));
		Funcionario f = new Funcionario(null, pj, "Dev-06", bd.cDesenvolvedor, LocalDate.now(), 0.,
				bd.cDesenvolvedor.getSalarioBase());
		pessoaService.save(pj);
		funcionarioService.save(f);
		// executa
		PageRequest pageRequest = PageRequest.of(0, 8);
		Page<FuncionarioListDTO> result = funcionarioService.findBy(TipoPessoa.PESSOA_JURIDICA,
				bd.cDesenvolvedor.getNomeCargo(), null, null, pageRequest);

		// verifica
		assertThat(result.getNumberOfElements(), equalTo(1));
		assertTrue(result.get().allMatch(fd -> fd.getCargo().equals(bd.cDesenvolvedor.getNomeCargo())));
		assertTrue(result.get().allMatch(fd -> fd.getTipo().equals(TipoPessoa.PESSOA_JURIDICA.getDescricao())));
	}

	@Transactional
	@Test
	public void testaBuscaParamiterizadaCargoAndNome() {

		cenarioParaBuscaParamiterizada();

		// executa
		PageRequest pageRequest = PageRequest.of(0, 8);
		Page<FuncionarioListDTO> result = funcionarioService.findBy(null, bd.cDesenvolvedor.getNomeCargo(), "MaRiA",
				null, pageRequest);

		// verifica
		assertThat(result.getNumberOfElements(), equalTo(2));
		assertTrue(result.get().allMatch(f -> f.getCargo().equals(bd.cDesenvolvedor.getNomeCargo())));
		assertFalse(result.get().anyMatch(f -> f.getNome().equals("Maria")));
	}

	@Transactional
	@Test
	public void testaBuscaParamiterizadaMatricula() {

		cenarioParaBuscaParamiterizada();

		// executa
		PageRequest pageRequest = PageRequest.of(0, 8);
		Page<FuncionarioListDTO> result = funcionarioService.findBy(null, null, null, "adm", pageRequest);
		// verifica
		assertThat(result.getNumberOfElements(), equalTo(4));
		assertTrue(result.get().allMatch(f -> f.getMatricula().toLowerCase().contains("adm")));
	}

}
