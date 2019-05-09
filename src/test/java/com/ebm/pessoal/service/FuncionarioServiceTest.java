package com.ebm.pessoal.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import javax.transaction.Transactional;

import org.junit.After;
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
import com.ebm.pessoal.domain.Cargo;
import com.ebm.pessoal.domain.Cidade;
import com.ebm.pessoal.domain.Endereco;
import com.ebm.pessoal.domain.Estado;
import com.ebm.pessoal.domain.Funcionario;
import com.ebm.pessoal.domain.PessoaFisica;
import com.ebm.pessoal.domain.PessoaJuridica;
import com.ebm.pessoal.domain.RG;
import com.ebm.pessoal.domain.TipoPessoa;
import com.ebm.pessoal.dtos.FuncionarioListDTO;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class FuncionarioServiceTest {

	@Autowired
	private FuncionarioService funcionarioService;
	@Autowired
	private PessoaService pessoaService;

	private Estado estadoGO;
	private Cidade goiania;
	private Endereco endereco1;
	private PessoaFisica pf1;
	private PessoaJuridica pj1;
	private Funcionario ff1;
	private Funcionario fj1;
	private Cargo cDesenvolvedor;
	private Cargo cAdministrador;

	@Before
	public void setUp() {
		funcionarioService.deleteAll();
		pessoaService.deleteAll(true);

		estadoGO = new Estado(null, "GO", "Goias");
		goiania = new Cidade(null, "Goiania", estadoGO);
		endereco1 = new Endereco(null, "Test rua tal", "Centro", goiania, "123", "prox ao carai", "12345678",
				"Endereco residencial", true);
		pf1 = new PessoaFisica(null, "Joao Da Silva", "02142627668", LocalDate.of(1990, 4, 30),
				new RG("23123", "SSP", estadoGO), "Brasileira", goiania);
		pj1 = new PessoaJuridica(null, "Lanches", "64935609000135", "Lanches ME", "inscricaoEstadual1",
				"inscricaoMunicipal1");

		pf1.getEndereco().add(endereco1);
		pf1.getTelefone().add(Utils.getRandomTelefone(true));
		pj1.getEndereco().add(endereco1);
		pj1.getTelefone().add(Utils.getRandomTelefone(true));
		pj1.getEmail().add(Utils.getRandomEmail(pj1, true));
		pf1.getEmail().add(Utils.getRandomEmail(pf1, true));
		cDesenvolvedor = new Cargo(null, "Desenvolvedor", BigDecimal.valueOf(2000), "rsats");
		cAdministrador = new Cargo(null, "Administrador", BigDecimal.valueOf(5000), "tes");
		ff1 = new Funcionario(null, pf1, "dev-432", cDesenvolvedor, LocalDate.now().minusWeeks(1), 0.,
				cDesenvolvedor.getSalarioBase());
		fj1 = new Funcionario(null, pj1, "adm-01", cAdministrador, LocalDate.now().minusDays(2), 0.1,
				cAdministrador.getSalarioBase());
	}

	@After
	public void setDown() {
		funcionarioService.deleteAll();
		pessoaService.deleteAll(true);
	}

	@Test
	public void testInsercaoSemPessoaDevLancarException() {

		try {
			ff1.setPessoa(null);
			ff1 = funcionarioService.save(ff1);
			fail("Falha tentando inserir cliente sem pessoa associada, era esperado Data Integration Exception");

		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(FuncionarioService.DATAINTEGRITY_EMPLOYEWITHOUTPERSON));
		}

	}

	@Test
	public void testInsersaoComMatriculaReptida() {
		funcionarioService.save(ff1);
		try {
			fj1.setMatricula(ff1.getMatricula());
			funcionarioService.save(fj1);
			fail();
		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(FuncionarioService.DATAINTEGRITY_DUPLICATEMATRICULA));
		}
	}

	@Test
	public void testUpdateComMatriculaReptida() {
		fj1 = funcionarioService.save(fj1);
		ff1 = funcionarioService.save(ff1);
		try {
			ff1.setMatricula(fj1.getMatricula());
			funcionarioService.save(ff1);
			fail();
		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(FuncionarioService.DATAINTEGRITY_DUPLICATEMATRICULA));
		}
	}

	@Test
	public void testInsercaoComPessoaNaoPersistida() {

		funcionarioService.save(ff1);

		assertNotNull(ff1.getId());
		assertNotNull(ff1.getPessoa().getId());
		assertThat(ff1.getId(), equalTo(ff1.getPessoa().getId()));
	}

	@Test
	public void testInsercaoComPessoaJaPersistida() {

		pf1 = (PessoaFisica) pessoaService.save(pf1);

		ff1.setPessoa(pf1);
		ff1 = funcionarioService.save(ff1);

		assertNotNull(ff1.getId());
		assertNotNull(ff1.getPessoa().getId());
		assertThat(ff1.getId(), equalTo(ff1.getPessoa().getId()));
	}

	@Test
	public void testInsercaoClienteComPessoaQueJaPertenceAOutroCliente() {
		ff1.setPessoa(fj1.getPessoa());
		ff1 = funcionarioService.save(ff1);

		try {
			fj1 = funcionarioService.save(fj1);
			fail("Falha insercao de cliente com pessoa que ja pertence a outro cliente, deve lançar DataIntegratyExcpetion");
		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(FuncionarioService.DATAINTEGRITY_DUPLICATEPERSON));
		}

	}

	@Test
	public void testUpdateSemMudarPessoa() {
		ff1 = funcionarioService.save(ff1);
		ff1.setCargo(cAdministrador);
		;
		ff1.getPessoa().setNome("novonome");
		ff1 = funcionarioService.save(ff1);

		assertThat(ff1.getCargo(), equalTo(cAdministrador));
		assertThat(ff1.getPessoa().getNome(), equalTo("novonome"));
	}

	@Test
	public void testUpdateMudarPessoa() {
		ff1 = funcionarioService.save(ff1);
		ff1.setPessoa(pj1);
		try {
			ff1 = funcionarioService.save(ff1);
			fail();
		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(FuncionarioService.DATAINTEGRITY_CHANCEPERSON));
		}
	}


	@Test
	public void testUpdateClienteComPessoaPertenceOutroCliente() {
		ff1 = funcionarioService.save(ff1);
		fj1 = funcionarioService.save(fj1);

		ff1.setPessoa(fj1.getPessoa());
		try {
			ff1 = funcionarioService.save(ff1);
			fail("Falha insercao de cliente com pessoa que ja pertence a outro cliente, deve lançar DataIntegratyExcpetion");
		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(FuncionarioService.DATAINTEGRITY_DUPLICATEPERSON));
		}
	}
	
	@Test
	public void testFindCnpj() {
		funcionarioService.save(fj1);
		
		Funcionario result = funcionarioService.findByCpfOrCnpj("64935609000135");
		
		assertNotNull(result.getId());
		assertThat(((PessoaJuridica) result.getPessoa()).getCnpj(), equalTo("64935609000135"));
	}
	@Test
	public void testFindCPF() {
		funcionarioService.save(ff1);
		
		Funcionario result = funcionarioService.findByCpfOrCnpj(((PessoaFisica) ff1.getPessoa()).getCpf());
		
		assertNotNull(result.getId());
		assertThat(((PessoaFisica) result.getPessoa()).getCpf(), equalTo(((PessoaFisica) result.getPessoa()).getCpf()));
	}
	
	@Test
	public void testFindCPFEx() {
	
		try {
			funcionarioService.findByCpfOrCnpj(((PessoaFisica) ff1.getPessoa()).getCpf());
			fail();
		}catch(ObjectNotFoundException ex) {
			assertThat(ex.getMessage(), equalTo(PessoaService.NOT_FOUND_DOCUMENT + ((PessoaFisica) ff1.getPessoa()).getCpf())  );
		}
	}
	private void cenarioParaBuscaParamiterizada() {
		PessoaFisica pf2 = new PessoaFisica(null, "Joao Snow", "52935546032", LocalDate.of(1995, 3, 30),
				new RG("3234", "SSP", estadoGO), "Brasileira", goiania);
		PessoaFisica pf3 = new PessoaFisica(null, "Maria Silva", "07952632019", LocalDate.of(1980, 4, 30),
				new RG("54345", "SSP", estadoGO), "Brasileira", goiania);
		PessoaFisica pf4 = new PessoaFisica(null, "Maria Carvalho", "58522943060", LocalDate.of(1990, 1, 30),
				new RG("4523", "SSP", estadoGO), "Brasileira", goiania);
		PessoaJuridica pj2 = new PessoaJuridica(null, "Juniscleids ME", "18038642000145", "Juniscleids ME",
				"inscricaoEstadual2", "inscricaoMunicipal2");
		PessoaJuridica pj3 = new PessoaJuridica(null, "Joao Dev", "46530490000139", "Profissionais ME",
				"inscricaoEstadual3", "inscricaoEstadual3");
		PessoaJuridica pj4 = new PessoaJuridica(null, "Mercado ME", "84912087000163", "Mercado ME",
				"inscricaoEstadual4", "inscricaoMunicipal4");
		Arrays.asList(pf2, pf3, pf4, pj2, pj3, pj4).forEach(p -> {
			p.getEndereco().add(endereco1);
			p.getTelefone().add(Utils.getRandomTelefone(true));
			p.getEmail().add(Utils.getRandomEmail(p,true));
		});

		Funcionario ff2 = new Funcionario(null, pf2, "dev-02", cDesenvolvedor, LocalDate.now().minusYears(1), 0.,
				cDesenvolvedor.getSalarioBase());
		Funcionario ff3 = new Funcionario(null, pf3, "dev-03", cDesenvolvedor, LocalDate.now().minusYears(1), 0.,
				cDesenvolvedor.getSalarioBase());
		Funcionario ff4 = new Funcionario(null, pf4, "dev-04", cDesenvolvedor, LocalDate.now().minusYears(1), 0.,
				cDesenvolvedor.getSalarioBase());

		Funcionario fj2 = new Funcionario(null, pj2, "adm-02", cAdministrador, LocalDate.now().minusYears(1), 0.,
				cAdministrador.getSalarioBase());
		Funcionario fj3 = new Funcionario(null, pj3, "adm-03", cAdministrador, LocalDate.now().minusYears(1), 0.,
				cAdministrador.getSalarioBase());
		Funcionario fj4 = new Funcionario(null, pj4, "adm-04", cAdministrador, LocalDate.now().minusYears(1), 0.,
				cAdministrador.getSalarioBase());

		funcionarioService.saveAll(Arrays.asList(ff1, ff2, ff3, ff4, fj1, fj2, fj3, fj4));
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
		Page<FuncionarioListDTO> result = funcionarioService.findBy(null, cAdministrador.getNomeCargo(), null, null,
				pageRequest);

		// verifica
		assertThat(result.getNumberOfElements(), equalTo(4));
		assertTrue(result.get().allMatch(f -> f.getCargo().equals(cAdministrador.getNomeCargo())));
		assertFalse(result.get().anyMatch(f -> f.getCargo().equals(cDesenvolvedor.getNomeCargo())));
	}

	@Transactional
	@Test
	public void testaBuscaParamiterizadaCargoAndTipoNothing() {

		cenarioParaBuscaParamiterizada();

		// executa
		PageRequest pageRequest = PageRequest.of(0, 8);
		Page<FuncionarioListDTO> result = funcionarioService.findBy(TipoPessoa.PESSOA_FISICA, cAdministrador.getNomeCargo(),
				null, null, pageRequest);

		// verifica
		assertThat(result.getNumberOfElements(), equalTo(0));
	}

	@Transactional
	@Test
	public void testaBuscaParamiterizadaCargoAndTipo() {

		cenarioParaBuscaParamiterizada();
		PessoaJuridica pj = new PessoaJuridica(null, "Dev Me", "93811961000167", "DevME", null, null);
		pj.getEndereco().add(endereco1);
		pj.getTelefone().add(Utils.getRandomTelefone(true));
		pj.getEmail().add(Utils.getRandomEmail(pj, true));
		Funcionario f = new Funcionario(null, pj, "Dev-06", cDesenvolvedor, LocalDate.now(), 0.,
				cDesenvolvedor.getSalarioBase());
		funcionarioService.save(f);
		// executa
		PageRequest pageRequest = PageRequest.of(0, 8);
		Page<FuncionarioListDTO> result = funcionarioService.findBy(TipoPessoa.PESSOA_JURIDICA, cDesenvolvedor.getNomeCargo(),
				null, null, pageRequest);

		// verifica
		assertThat(result.getNumberOfElements(), equalTo(1));
		assertTrue(result.get().allMatch(fd -> fd.getCargo().equals(cDesenvolvedor.getNomeCargo())));
		assertTrue(result.get().allMatch(fd -> fd.getTipo().equals(TipoPessoa.PESSOA_JURIDICA.getDescricao())));
	}

	@Transactional
	@Test
	public void testaBuscaParamiterizadaCargoAndNome() {

		cenarioParaBuscaParamiterizada();

		// executa
		PageRequest pageRequest = PageRequest.of(0, 8);
		Page<FuncionarioListDTO> result = funcionarioService.findBy(null, cDesenvolvedor.getNomeCargo(), "MaRiA", null,
				pageRequest);

		// verifica
		assertThat(result.getNumberOfElements(), equalTo(2));
		assertTrue(result.get().allMatch(f -> f.getCargo().equals(cDesenvolvedor.getNomeCargo())));
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
