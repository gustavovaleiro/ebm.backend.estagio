package com.ebm.pessoal.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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

import com.ebm.estoque.service.interfaces.CategoriaItemService;
import com.ebm.exceptions.DataIntegrityException;
import com.ebm.exceptions.ObjectNotFoundException;
import com.ebm.geral.service.PopulaBD;
import com.ebm.pessoal.domain.Fornecedor;
import com.ebm.pessoal.domain.PessoaFisica;
import com.ebm.pessoal.domain.PessoaJuridica;
import com.ebm.pessoal.domain.TipoPessoa;
import com.ebm.pessoal.dtos.FornecedorListDTO;
import com.ebm.pessoal.service.interfaces.FornecedorService;

@ActiveProfiles("testauto")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class FornecedorServiceTest {

	@Autowired
	private FornecedorService fornecedorService;
	@Autowired
	private PessoaService pessoaService;
	@Autowired
	private CategoriaItemService categoriaService;
	@Autowired
	private PopulaBD bd;

	@Before
	public void setUp() {

		bd.instanciaFornecedores(true);
		categoriaService.saveAll(Arrays.asList(bd.cat1, bd.cat2, bd.cat3, bd.cat4));

		pessoaService.saveAll(Arrays.asList(bd.pf1, bd.pj1));
	}

	@Transactional
	@Test
	public void testInsercaoSemPessoaDevLancarException() {

		try {
			bd.forf1.setPessoa(null);
			fornecedorService.save(bd.forf1);
			fail("Falha tentando inserir fornecedor sem pessoa associada, era esperado Data Integration Exception");

		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(FornecedorService.DATAINTEGRITY_FORNECEDORWITHOUTPERSON));
		}
	}

	@Transactional
	@Test
	public void testInsercaoComPessoaNaoPersistida() {

		fornecedorService.save(bd.forf1);

		assertNotNull(bd.forf1.getId());
		assertNotNull(bd.forf1.getPessoa().getId());
		assertThat(bd.forf1.getId(), equalTo(bd.forf1.getPessoa().getId()));
	}

	@Transactional
	@Test
	public void testInsercaoComPessoaJaPersistida() {

		bd.pf1 = (PessoaFisica) pessoaService.save(bd.pf1);

		bd.forf1.setPessoa(bd.pf1);
		bd.forf1 = fornecedorService.save(bd.forf1);

		assertNotNull(bd.forf1.getId());
		assertNotNull(bd.forf1.getPessoa().getId());
		assertThat(bd.forf1.getId(), equalTo(bd.forf1.getPessoa().getId()));
	}

	@Transactional
	@Test
	public void testInsercaoFornecedorComPessoaQueJaPertenceAOutroFornecedor() {
		bd.forf1.setPessoa(bd.forj1.getPessoa());
		bd.forf1 = fornecedorService.save(bd.forf1);

		try {
			bd.forj1 = fornecedorService.save(bd.forj1);
			fail("Falha insercao de fornecedor com pessoa que ja pertence a outro fornecedor, deve lançar DataIntegratyExcpetion");
		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(FornecedorService.DATAINTEGRITY_DUPLICATEPERSON));
		}

	}

	@Transactional
	@Test
	public void testUpdateSemMudarPessoa() {
		bd.forf1 = fornecedorService.save(bd.forf1);
		bd.forf1.getPessoa().setNome("novonome");
		bd.forf1 = fornecedorService.save(bd.forf1);

		assertThat(bd.forf1.getPessoa().getNome(), equalTo("novonome"));
	}

	@Transactional
	@Test
	public void testUpdateMudarPessoa() {
		bd.forf1 = fornecedorService.save(bd.forf1);
		bd.forf1.setPessoa(bd.pj1);

		try {
			bd.forf1 = fornecedorService.save(bd.forf1);
			fail();
		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(FornecedorService.DATAINTEGRITY_CHANCEPERSON));
		}
	}

	@Transactional
	@Test
	public void testUpdateFornecedorComPessoaPertenceOutroFornecedor() {
		bd.forf1 = fornecedorService.save(bd.forf1);
		bd.forj1 = fornecedorService.save(bd.forj1);

		bd.forf1.setPessoa(bd.forj1.getPessoa());
		try {
			bd.forf1 = fornecedorService.save(bd.forf1);
			fail("Falha insercao de fornecedor com pessoa que ja pertence a outro fornecedor, deve lançar DataIntegratyExcpetion");
		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(FornecedorService.DATAINTEGRITY_DUPLICATEPERSON));
		}
	}

	@Transactional
	@Test
	public void findById() {
		fornecedorService.save(bd.forf1);

		Fornecedor result = fornecedorService.findById(bd.forf1.getId());

		assertThat(bd.forf1.getId(), equalTo(result.getId()));
	}

	@Transactional
	@Test
	public void findByIdNull() {
		try {
			fornecedorService.findById(bd.forf1.getId());
		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(FornecedorService.DATAINTEGRITY_IDNULL));
		}
	}

	@Transactional
	@Test
	public void findByIdONFEX() {
		try {
			fornecedorService.findById(4);
		} catch (ObjectNotFoundException ex) {
			assertThat(ex.getMessage(), equalTo(FornecedorService.ONFE_BYID + 4));
		}
	}

	@Transactional
	@Test
	public void testFindCnpj() {
		fornecedorService.save(bd.forj1);

		Fornecedor result = fornecedorService.findByCpfOrCnpj(((PessoaJuridica) bd.forj1.getPessoa()).getCnpj());

		assertNotNull(result.getId());
		assertThat(((PessoaJuridica) result.getPessoa()).getCnpj(),
				equalTo(((PessoaJuridica) bd.forj1.getPessoa()).getCnpj()));
	}

	@Transactional
	@Test
	public void testFindCPF() {
		fornecedorService.save(bd.forf1);

		Fornecedor result = fornecedorService.findByCpfOrCnpj(((PessoaFisica) bd.forf1.getPessoa()).getCpf());

		assertNotNull(result.getId());
		assertThat(((PessoaFisica) result.getPessoa()).getCpf(), equalTo(((PessoaFisica) result.getPessoa()).getCpf()));
	}

	@Transactional
	@Test
	public void testFindCPFEx() {

		try {
			fornecedorService.findByCpfOrCnpj("05909561162");
			fail();
		} catch (ObjectNotFoundException ex) {
			assertThat(ex.getMessage(), equalTo(PessoaService.NOT_FOUND_DOCUMENT + "05909561162"));
		}
	}

	private void cenarioParaBuscaParamiterizada() {

		pessoaService.saveAll(Arrays.asList(bd.pf2, bd.pf3, bd.pf4, bd.pf4, bd.pf5, bd.pj2, bd.pj3, bd.pj4));

		fornecedorService.saveAll(Arrays.asList(bd.forf1, bd.forf2, bd.forf3, bd.forf4, bd.forj1, bd.forj2, bd.forj3,
				bd.forj4, bd.forf5));

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
	@Test // bd.forj1 bd.forj3 bd.forj4
	public void testaBuscaParamiterizadaUmCategoria() {

		cenarioParaBuscaParamiterizada();
		Set<Integer> cats = new HashSet<>(Arrays.asList(bd.cat1.getId()));
		// executa
		PageRequest pageRequest = PageRequest.of(0, 8);
		Page<FornecedorListDTO> result = fornecedorService.findBy(null, null, cats, pageRequest);

		// verifica
		assertThat(result.getNumberOfElements(), equalTo(4));
		assertTrue(result.get().anyMatch(f -> f.getId() == bd.forj1.getId()));
		assertTrue(result.get().anyMatch(f -> f.getId() == bd.forj4.getId()));
		assertTrue(result.get().anyMatch(f -> f.getId() == bd.forj3.getId()));
		assertTrue(result.get().anyMatch(f -> f.getId() == bd.forf5.getId()));

	}

	@Transactional
	@Test // bd.forj1
	public void testaBuscaParamiterizadaDuasCategoria() {

		cenarioParaBuscaParamiterizada();
		Set<Integer> cats = new HashSet<>(Arrays.asList(bd.cat1.getId(), bd.cat2.getId()));
		// executa
		PageRequest pageRequest = PageRequest.of(0, 8);
		Page<FornecedorListDTO> result = fornecedorService.findBy(null, null, cats, pageRequest);
		// verifica
		assertThat(result.getNumberOfElements(), equalTo(4));
		assertTrue(result.get().anyMatch(f -> f.getId() == bd.forj1.getId()));

	}

	@Transactional
	@Test // bd.forj1
	public void testaBuscaParamiterizadaCategoriaNome() {

		cenarioParaBuscaParamiterizada();
		Set<Integer> cats = new HashSet<>(Arrays.asList(bd.cat1.getId()));
		// executa
		PageRequest pageRequest = PageRequest.of(0, 8);
		Page<FornecedorListDTO> result = fornecedorService.findBy(null, "JOAO", cats, pageRequest);

		// verifica
		assertThat(result.getNumberOfElements(), equalTo(1));
		assertTrue(result.get().anyMatch(f -> f.getId() == bd.forj3.getId()));

	}

	@Transactional
	@Test // bd.forj1
	public void testaBuscaParamiterizadaCategoriaTipo() {

		cenarioParaBuscaParamiterizada();
		Set<Integer> cats = new HashSet<>(Arrays.asList(bd.cat1.getId()));
		// executa
		PageRequest pageRequest = PageRequest.of(0, 8);
		Page<FornecedorListDTO> result = fornecedorService.findBy(TipoPessoa.PESSOA_FISICA, null, cats, pageRequest);

		// verifica
		assertThat(result.getNumberOfElements(), equalTo(1));
		assertTrue(result.get().anyMatch(f -> f.getId() == bd.forf5.getId()));

	}
}
