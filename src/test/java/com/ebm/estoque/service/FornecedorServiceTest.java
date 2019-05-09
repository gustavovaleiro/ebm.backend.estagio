package com.ebm.estoque.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
import org.springframework.transaction.annotation.Transactional;

import com.ebm.Utils;
import com.ebm.estoque.domain.CategoriaItem;
import com.ebm.estoque.domain.Fornecedor;
import com.ebm.estoque.dtos.FornecedorListDTO;
import com.ebm.estoque.service.interfaces.CategoriaItemService;
import com.ebm.estoque.service.interfaces.FornecedorService;
import com.ebm.exceptions.DataIntegrityException;
import com.ebm.exceptions.ObjectNotFoundException;
import com.ebm.pessoal.domain.Cidade;
import com.ebm.pessoal.domain.Endereco;
import com.ebm.pessoal.domain.Estado;
import com.ebm.pessoal.domain.PessoaFisica;
import com.ebm.pessoal.domain.PessoaJuridica;
import com.ebm.pessoal.domain.RG;
import com.ebm.pessoal.domain.TipoPessoa;
import com.ebm.pessoal.service.PessoaService;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class FornecedorServiceTest {

	@Autowired
	private FornecedorService fornecedorService;
	@Autowired
	private PessoaService pessoaService;
	@Autowired
	private CategoriaItemService categoriaService;
	private Estado estadoGO;
	private Cidade goiania;
	private Endereco endereco1;
	private PessoaFisica pf1;
	private PessoaJuridica pj1;
	private Fornecedor ff1;
	private Fornecedor fj1;
	private CategoriaItem cat1;
	private CategoriaItem cat2;
	private CategoriaItem cat3;
	private CategoriaItem cat4;
	private Fornecedor ff2;
	private Fornecedor ff3;
	private Fornecedor ff4;
	private Fornecedor fj2;
	private Fornecedor fj3;
	private Fornecedor fj4;
	private Fornecedor ff5;

	@Before
	public void setUp() {

		fornecedorService.deleteAll();
		categoriaService.deleteAll();
		pessoaService.deleteAll(true);

		cat1 = new CategoriaItem(null, "Informatica");
		cat3 = new CategoriaItem(null, "Cama");
		cat2 = new CategoriaItem(null, "Eletrodomesticos");
		cat4 = new CategoriaItem(null, "Banho");

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

		categoriaService.saveAll(Arrays.asList(cat1, cat2, cat3, cat4));
		ff1 = new Fornecedor(null, pf1);
		ff1.getCategorias().addAll(new HashSet<>(Arrays.asList(cat3, cat4)));
		fj1 = new Fornecedor(null, pj1);
		fj1.getCategorias().addAll(new HashSet<>(Arrays.asList(cat1, cat2)));
	}

	@After
	public void setDown() {

		fornecedorService.deleteAll();
		categoriaService.deleteAll();
		pessoaService.deleteAll(true);
	}

	@Test
	public void testInsercaoSemPessoaDevLancarException() {

		try {
			ff1.setPessoa(null);
			fornecedorService.save(ff1);
			fail("Falha tentando inserir fornecedor sem pessoa associada, era esperado Data Integration Exception");

		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(FornecedorService.DATAINTEGRITY_FORNECEDORWITHOUTPERSON));
		}
	}

	@Test
	public void testInsercaoComPessoaNaoPersistida() {

		fornecedorService.save(ff1);

		assertNotNull(ff1.getId());
		assertNotNull(ff1.getPessoa().getId());
		assertThat(ff1.getId(), equalTo(ff1.getPessoa().getId()));
	}

	@Test
	public void testInsercaoCategoriaApenasId() {
		Set<CategoriaItem> cats = ff1.getCategorias();
		ff1.getCategorias().forEach(c -> c.setNome(null));

		ff1 = fornecedorService.save(ff1);

		assertTrue(ff1.getCategorias().stream().allMatch(c -> c.getNome() != null));
	}

	@Test
	public void testInsercaoComPessoaJaPersistida() {

		pf1 = (PessoaFisica) pessoaService.save(pf1);

		ff1.setPessoa(pf1);
		ff1 = fornecedorService.save(ff1);

		assertNotNull(ff1.getId());
		assertNotNull(ff1.getPessoa().getId());
		assertThat(ff1.getId(), equalTo(ff1.getPessoa().getId()));
	}

	@Test
	public void testInsercaoFornecedorComPessoaQueJaPertenceAOutroFornecedor() {
		ff1.setPessoa(fj1.getPessoa());
		ff1 = fornecedorService.save(ff1);

		try {
			fj1 = fornecedorService.save(fj1);
			fail("Falha insercao de fornecedor com pessoa que ja pertence a outro fornecedor, deve lançar DataIntegratyExcpetion");
		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(FornecedorService.DATAINTEGRITY_DUPLICATEPERSON));
		}

	}

	@Test
	public void testUpdateSemMudarPessoa() {
		ff1 = fornecedorService.save(ff1);
		ff1.getPessoa().setNome("novonome");
		ff1 = fornecedorService.save(ff1);

		assertThat(ff1.getPessoa().getNome(), equalTo("novonome"));
	}

	@Test
	public void testUpdateMudarPessoa() {
		ff1 = fornecedorService.save(ff1);
		ff1.setPessoa(pj1);

		try {
			ff1 = fornecedorService.save(ff1);
			fail();
		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(FornecedorService.DATAINTEGRITY_CHANCEPERSON));
		}
	}

	@Test
	public void testUpdateFornecedorComPessoaPertenceOutroFornecedor() {
		ff1 = fornecedorService.save(ff1);
		fj1 = fornecedorService.save(fj1);

		ff1.setPessoa(fj1.getPessoa());
		try {
			ff1 = fornecedorService.save(ff1);
			fail("Falha insercao de fornecedor com pessoa que ja pertence a outro fornecedor, deve lançar DataIntegratyExcpetion");
		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(FornecedorService.DATAINTEGRITY_DUPLICATEPERSON));
		}
	}

	@Test
	public void findById() {
		fornecedorService.save(ff1);

		Fornecedor result = fornecedorService.findById(ff1.getId());

		assertThat(ff1.getId(), equalTo(result.getId()));
	}

	@Test
	public void findByIdNull() {
		try {
			fornecedorService.findById(ff1.getId());
		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(FornecedorService.DATAINTEGRITY_IDNULL));
		}
	}

	@Test
	public void findByIdONFEX() {
		try {
			fornecedorService.findById(4);
		} catch (ObjectNotFoundException ex) {
			assertThat(ex.getMessage(), equalTo(FornecedorService.ONFE_BYID + 4));
		}
	}

	@Test
	public void testFindCnpj() {
		fornecedorService.save(fj1);

		Fornecedor result = fornecedorService.findByCpfOrCnpj(((PessoaJuridica) fj1.getPessoa()).getCnpj());

		assertNotNull(result.getId());
		assertThat(((PessoaJuridica) result.getPessoa()).getCnpj(),
				equalTo(((PessoaJuridica) fj1.getPessoa()).getCnpj()));
	}

	@Test
	public void testFindCPF() {
		fornecedorService.save(ff1);

		Fornecedor result = fornecedorService.findByCpfOrCnpj(((PessoaFisica) ff1.getPessoa()).getCpf());

		assertNotNull(result.getId());
		assertThat(((PessoaFisica) result.getPessoa()).getCpf(), equalTo(((PessoaFisica) result.getPessoa()).getCpf()));
	}

	@Test
	public void testFindCPFEx() {

		try {
			fornecedorService.findByCpfOrCnpj(((PessoaFisica) ff1.getPessoa()).getCpf());
			fail();
		} catch (ObjectNotFoundException ex) {
			assertThat(ex.getMessage(),
					equalTo(PessoaService.NOT_FOUND_DOCUMENT + ((PessoaFisica) ff1.getPessoa()).getCpf()));
		}
	}
	
	
	@Test
	public void testaDeleteDevLancEx() {
		fj1 = fornecedorService.save(fj1);
		try {
			fornecedorService.delete(fj1.getId());
			fail();
		} catch ( DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(FornecedorService.DATAINTEGRITY_FORNECEDORHASCATEGORIA));
		}
	}

	private void cenarioParaBuscaParamiterizada() {
		PessoaFisica pf2 = new PessoaFisica(null, "Joao Snow", "52935546032", LocalDate.of(1995, 3, 30),
				new RG("3234", "SSP", estadoGO), "Brasileira", goiania);
		PessoaFisica pf3 = new PessoaFisica(null, "Maria Silva", "07952632019", LocalDate.of(1980, 4, 30),
				new RG("54345", "SSP", estadoGO), "Brasileira", goiania);
		PessoaFisica pf4 = new PessoaFisica(null, "Maria Carvalho", "58522943060", LocalDate.of(1990, 1, 30),
				new RG("4523", "SSP", estadoGO), "Brasileira", goiania);
		PessoaFisica pf5 = new PessoaFisica(null, "HEY", "05909561162", LocalDate.of(1994, 3, 30),
				new RG("34", "ssp", estadoGO), "Brasileira", goiania);
		PessoaJuridica pj2 = new PessoaJuridica(null, "Juniscleids ME", "18038642000145", "Juniscleids ME",
				"inscricaoEstadual2", "inscricaoMunicipal2");
		PessoaJuridica pj3 = new PessoaJuridica(null, "Joao Dev", "46530490000139", "Profissionais ME",
				"inscricaoEstadual3", "inscricaoEstadual3");
		PessoaJuridica pj4 = new PessoaJuridica(null, "Mercado ME", "84912087000163", "Mercado ME",
				"inscricaoEstadual4", "inscricaoMunicipal4");
		Arrays.asList(pf2, pf3, pf4, pj2, pj3, pj4, pf5).forEach(p -> {
			p.getEndereco().add(endereco1);
			p.getTelefone().add(Utils.getRandomTelefone(true));
			p.getEmail().add(Utils.getRandomEmail(p, true));
		});

		ff2 = new Fornecedor(null, pf2);
		ff3 = new Fornecedor(null, pf3);
		ff4 = new Fornecedor(null, pf4);
		fj2 = new Fornecedor(null, pj2);
		fj3 = new Fornecedor(null, pj3);
		fj4 = new Fornecedor(null, pj4);
		ff5 = new Fornecedor(null, pf5);

		fornecedorService.saveAll(Arrays.asList(ff1, ff2, ff3, ff4, fj1, fj2, fj3, fj4, ff5));

		Arrays.asList(ff3, ff4).forEach(f -> f.getCategorias().addAll(Arrays.asList(cat3)));
		Arrays.asList(fj3, fj4, ff5).forEach(f -> f.getCategorias().addAll(Arrays.asList(cat1)));
	}

	@Transactional
	@Test
	public void testaBuscaParamiterizadaPessoaFisica() {

		cenarioParaBuscaParamiterizada();

		// executa
		PageRequest pageRequest = PageRequest.of(0, 8);
		Page<FornecedorListDTO> result = fornecedorService.findBy(TipoPessoa.PESSOA_FISICA, null, null, pageRequest);

		// verifica
		assertThat(result.getNumberOfElements(), equalTo(5));
		assertTrue(result.get().allMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_FISICA.getDescricao())));
		assertFalse(result.get().anyMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_JURIDICA.getDescricao())));
	}

	@Transactional
	@Test
	public void testaBuscaParamiterizadaPessoaJuridica() {

		cenarioParaBuscaParamiterizada();

		// executa
		PageRequest pageRequest = PageRequest.of(0, 5);
		Page<FornecedorListDTO> result = fornecedorService.findBy(TipoPessoa.PESSOA_JURIDICA, null, null, pageRequest);

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
		PageRequest pageRequest = PageRequest.of(0, 9);
		Page<FornecedorListDTO> result = fornecedorService.findBy(null, null, null, pageRequest);

		// verifica
		assertThat(result.getNumberOfElements(), equalTo(9));
		assertTrue(result.get().anyMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_FISICA.getDescricao())));
		assertTrue(result.get().anyMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_JURIDICA.getDescricao())));
	}

	@Transactional
	@Test
	public void testaBuscaParamiterizadaNome() {

		cenarioParaBuscaParamiterizada();

		// executa
		PageRequest pageRequest = PageRequest.of(0, 8);
		Page<FornecedorListDTO> result = fornecedorService.findBy(null, "joao", null, pageRequest);

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
		Page<FornecedorListDTO> result = fornecedorService.findBy(TipoPessoa.PESSOA_FISICA, "joao", null, pageRequest);

		// verifica
		assertThat(result.getNumberOfElements(), equalTo(2));
		assertTrue(result.get().allMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_FISICA.getDescricao())));

	}

	@Transactional
	@Test // FJ1 FJ3 FJ4
	public void testaBuscaParamiterizadaUmCategoria() {

		cenarioParaBuscaParamiterizada();
		Set<Integer> cats = new HashSet<>(Arrays.asList(cat1.getId()));
		// executa
		PageRequest pageRequest = PageRequest.of(0, 8);
		Page<FornecedorListDTO> result = fornecedorService.findBy(null, null, cats, pageRequest);

		// verifica
		assertThat(result.getNumberOfElements(), equalTo(4));
		assertTrue(result.get().anyMatch(f -> f.getId() == fj1.getId()));
		assertTrue(result.get().anyMatch(f -> f.getId() == fj4.getId()));
		assertTrue(result.get().anyMatch(f -> f.getId() == fj3.getId()));
		assertTrue(result.get().anyMatch(f -> f.getId() == ff5.getId()));

	}

	@Transactional
	@Test // FJ1
	public void testaBuscaParamiterizadaDuasCategoria() {

		cenarioParaBuscaParamiterizada();
		Set<Integer> cats = new HashSet<>(Arrays.asList(cat1.getId(), cat2.getId()));
		// executa
		PageRequest pageRequest = PageRequest.of(0, 8);
		Page<FornecedorListDTO> result = fornecedorService.findBy(null, null, cats, pageRequest);
		// verifica
		assertThat(result.getNumberOfElements(), equalTo(4));
		assertTrue(result.get().anyMatch(f -> f.getId() == fj1.getId()));

	}

	@Transactional
	@Test // FJ1
	public void testaBuscaParamiterizadaCategoriaNome() {

		cenarioParaBuscaParamiterizada();
		Set<Integer> cats = new HashSet<>(Arrays.asList(cat1.getId()));
		// executa
		PageRequest pageRequest = PageRequest.of(0, 8);
		Page<FornecedorListDTO> result = fornecedorService.findBy(null, "JOAO", cats, pageRequest);
	
		// verifica
		assertThat(result.getNumberOfElements(), equalTo(1));
		assertTrue(result.get().anyMatch(f -> f.getId() == fj3.getId()));

	}

	@Transactional
	@Test // FJ1
	public void testaBuscaParamiterizadaCategoriaTipo() {

		cenarioParaBuscaParamiterizada();
		Set<Integer> cats = new HashSet<>(Arrays.asList(cat1.getId()));
		// executa
		PageRequest pageRequest = PageRequest.of(0, 8);
		Page<FornecedorListDTO> result = fornecedorService.findBy(TipoPessoa.PESSOA_FISICA, null, cats, pageRequest);
	
		// verifica
		assertThat(result.getNumberOfElements(), equalTo(1));
		assertTrue(result.get().anyMatch(f -> f.getId() == ff5.getId()));

	}
}
