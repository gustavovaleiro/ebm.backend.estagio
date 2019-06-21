package com.ebm.estoque.resource;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.ebm.BaseTest;
import com.ebm.estoque.domain.Movimentacao;
import com.ebm.estoque.domain.Produto;
import com.ebm.estoque.domain.ProdutoMovimentacao;
import com.ebm.estoque.domain.enums.TipoMovimentacao;
import com.ebm.estoque.dtos.MovimentacaoListDTO;
import com.ebm.geral.domain.RestResponsePage;
import com.ebm.geral.resource.exception.ValidationError;
import com.ebm.geral.service.PopulaBD;
import com.ebm.geral.utils.Utils;
import com.fasterxml.jackson.core.type.TypeReference;

public class MovimentacaoResourceTest extends BaseTest {

	@Autowired
	private PopulaBD bd;

	private final String ENDPOINT_BASE = "/movimentacoes";
	private final String BASE_AUTHORITY = "MOVIMENTACAO_";

	@Before
	public void setUp() {
		bd.instanciaMovimentacao(true);
		Arrays.asList(bd.p1, bd.p2, bd.p3).forEach(p -> ((Produto) p).setEstoque(5, 0, 10));

		bd.saveUnidade(Arrays.asList(bd.un1, bd.un2));
		bd.saveCategoria(Arrays.asList(bd.cat1, bd.cat2, bd.cat3, bd.cat4));
		bd.saveProduto(Arrays.asList(bd.p1, bd.p2, bd.p3));

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testInserNormalEntraComFornecedor() throws Exception {
		fornecedor();
		bd.ent1.getFornecedores().add(bd.forf1);
		util.testPostExpectCreated(ENDPOINT_BASE, bd.ent1).andDo(result -> {
			Movimentacao mov = bd.getMovimentacaoS()
					.findById(Integer.valueOf(util.getIdRedirect(result, ENDPOINT_BASE)));
			assertTrue(mov.getProdutos().containsAll(bd.ent1.getProdutos()));
			assertTrue(mov.getFornecedores().contains(bd.forf1));
		});

	}

	private void fornecedor() {
		bd.instanciaFornecedores(true);
		bd.savePessoa(Arrays.asList(bd.pf1));
		bd.saveFornecedores(Arrays.asList(bd.forf1));
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testInserNormalEntSemFornecedor() throws Exception {
		util.testPostExpectCreated(ENDPOINT_BASE, bd.sai1).andDo(result -> {
			Movimentacao mov = bd.getMovimentacaoS()
					.findById(Integer.valueOf(util.getIdRedirect(result, ENDPOINT_BASE)));
			assertTrue(mov.getProdutos().containsAll(bd.ent1.getProdutos()));
		});

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testInserNormalSaida() throws Exception {
		util.testPostExpectCreated(ENDPOINT_BASE, bd.sai1).andDo(result -> {
			Movimentacao mov = bd.getMovimentacaoS()
					.findById(Integer.valueOf(util.getIdRedirect(result, ENDPOINT_BASE)));
			assertTrue(mov.getProdutos().containsAll(bd.ent1.getProdutos()));
		});

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testInserNormalSaidaComFornecedor() throws Exception {
		fornecedor();
		bd.sai1.getFornecedores().add(bd.forf1);
		util.testPost(ENDPOINT_BASE, bd.sai1, status().isBadRequest());
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POdST" })
	public void testaInsercaoSemAuth() throws Exception {

		util.testPostExpectForbidden(ENDPOINT_BASE, bd.sai1);
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testaInsercaoErrosObjectNullEmptyGiveErrors() throws Exception {
		bd.ent1.setTipoMovimentacao(null);
		bd.ent1.setProdutoMovimentacao(null);
		util.testPost(ENDPOINT_BASE, bd.ent1, status().isUnprocessableEntity())
				.andExpect(jsonPath("$.errors.*", hasSize(3)));
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testaInsercaoErrosObjectOnlyEmptyGiveErrors() throws Exception {

		bd.ent1.setProdutoMovimentacao(new HashSet<>());
		util.testPost(ENDPOINT_BASE, bd.ent1, status().isUnprocessableEntity())
				.andExpect(jsonPath("$.errors.*", hasSize(1))).andDo(result -> {
					ValidationError error = util.getValidationErrorOf(result);

					assertTrue(error.getErrors().size() == 1);
					assertTrue(
							error.getErrors().stream().anyMatch(e -> e.getFieldName().equals("produtoMovimentacao")));
				});
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testaInsercaoErrosLength() throws Exception {

		bd.ent1.setDescricao(Utils.getRandomString(602));
		bd.ent1.setDocumento(Utils.getRandomString(1));
		util.testPost(ENDPOINT_BASE, bd.ent1, status().isUnprocessableEntity()).andDo(result -> {
			ValidationError error = util.getValidationErrorOf(result);

			assertTrue(error.getErrors().size() == 2);
			assertTrue(error.getErrors().stream().anyMatch(e -> e.getFieldName().equals("descricao")));
			assertTrue(error.getErrors().stream().anyMatch(e -> e.getFieldName().equals("documento")));
		});
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testaInsercaoErrosLengthTwo() throws Exception {

		bd.ent1.setDescricao(Utils.getRandomString(444));
		bd.ent1.setDocumento(Utils.getRandomString(42));
		util.testPost(ENDPOINT_BASE, bd.ent1, status().isUnprocessableEntity()).andDo(result -> {
			ValidationError error = util.getValidationErrorOf(result);

			assertTrue(error.getErrors().size() == 1);
			assertFalse(error.getErrors().stream().anyMatch(e -> e.getFieldName().equals("descricao")));
			assertTrue(error.getErrors().stream().anyMatch(e -> e.getFieldName().equals("documento")));
		});

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "PUT" })
	public void testaUpdate() throws Exception {
		bd.getMovimentacaoS().save(bd.ent1);

		bd.ent1.setDescricao("NOVONOME");

		util.testPutExpectNoContent(ENDPOINT_BASE + "/" + bd.ent1.getId(), bd.ent1);

		bd.ent1 = bd.getMovimentacaoS().findById(bd.ent1.getId());

		assertTrue(bd.ent1.getDescricao().equals("NOVONOME"));
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testaUpdateNoAuthority() throws Exception {

		util.testPutExpectedForbidden(ENDPOINT_BASE + "/1", bd.ent1);
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testaAlteracoesEstoquePorMovimentacao() throws Exception {
		// Preparação do cenario
		Integer estqAtual = this.bd.p1.getEstoqueAtual();
		bd.ent1.setProdutoMovimentacao(new HashSet<>());
		bd.sai1.setProdutoMovimentacao(new HashSet<>());
		Arrays.asList(bd.ent1, bd.sai1).forEach(m -> m.getProdutoMovimentacao()
				.add(new ProdutoMovimentacao(m, this.bd.p1, BigDecimal.valueOf(0), BigDecimal.valueOf(100), 2)));

		// test entrada alteração do estoque
		util.testPostExpectCreated(ENDPOINT_BASE, bd.ent1);
		Produto prod = (Produto) bd.getItemS().findById(bd.p1.getId());
		assertTrue(prod.getEstoqueAtual().equals(estqAtual + 2));

		// test saida alteração do estoque
		util.testPostExpectCreated(ENDPOINT_BASE, bd.sai1);
		prod = (Produto) bd.getItemS().findById(bd.p1.getId());
		assertTrue(prod.getEstoqueAtual().equals(estqAtual));

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testaFindById() throws Exception {
		bd.getMovimentacaoS().save(bd.ent1);
		util.testGetExpectedSucess(this.ENDPOINT_BASE, bd.ent1.getId());
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testaFindByIdExpectNotFound() throws Exception {
		util.testGet(this.ENDPOINT_BASE, 1, status().isNotFound());
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GETT" })
	public void testaFindByIdNoAuthority() throws Exception {
		bd.getMovimentacaoS().save(bd.ent1);
		util.testGet(this.ENDPOINT_BASE, bd.p1.getId(), status().isForbidden());
	}

	private void preparaTestParameterizado() {
		Arrays.asList(bd.p4, bd.p5, bd.p6, bd.p7).forEach(p -> ((Produto) p).setEstoque(5, 0, 10));
		bd.getItemS().saveAll(Arrays.asList(bd.p4, bd.p5, bd.p6, bd.p7));

		bd.ent2.setDocumento("notafiscal-01");
		bd.ent3.setDocumento("notafiscal-01");
		bd.sai2.setDocumento("Venda01");
		bd.sai3.setDocumento("Venda02");

		fornecedor();
		bd.ent2.getFornecedores().add(bd.forf1);

		bd.getMovimentacaoS().saveAll(Arrays.asList(bd.ent1, bd.ent2, bd.ent3, bd.sai1, bd.sai2, bd.sai3));
	}

	private RestResponsePage<MovimentacaoListDTO> getPage(MvcResult result) throws Exception {
		String content = result.getResponse().getContentAsString();
		RestResponsePage<MovimentacaoListDTO> responseList = this.util.objectMapper().readValue(content,
				new TypeReference<RestResponsePage<MovimentacaoListDTO>>() {
				});
		return responseList;
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindParamiterizadoTipoEntrada() throws Exception {
		preparaTestParameterizado();

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("tipo", TipoMovimentacao.ENTRADA.getDesc());

		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 3, 1).andDo(result -> {
			RestResponsePage<MovimentacaoListDTO> responseList = getPage(result);
			assertTrue(responseList.getContent().stream()
					.allMatch(p -> p.getTipo().equalsIgnoreCase(TipoMovimentacao.ENTRADA.getDesc())));
			assertFalse(responseList.getContent().stream()
					.anyMatch(p -> p.getTipo().equalsIgnoreCase(TipoMovimentacao.SAIDA.getDesc())));
		});

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindParamiterizadoTipoSaida() throws Exception {
		preparaTestParameterizado();

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("tipo", TipoMovimentacao.SAIDA.getDesc());

		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 3, 1).andDo(result -> {
			RestResponsePage<MovimentacaoListDTO> responseList = getPage(result);
			assertTrue(responseList.getContent().stream()
					.allMatch(p -> p.getTipo().equalsIgnoreCase(TipoMovimentacao.SAIDA.getDesc())));
			assertFalse(responseList.getContent().stream()
					.anyMatch(p -> p.getTipo().equalsIgnoreCase(TipoMovimentacao.ENTRADA.getDesc())));
		});

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindParamiterizadoDocumento() throws Exception {
		preparaTestParameterizado();

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("documento", "notafiscal");

		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 2, 1).andDo(result_ -> {
			RestResponsePage<MovimentacaoListDTO> result = getPage(result_);

			assertTrue(result.stream().allMatch(m -> m.getTipo().equals(TipoMovimentacao.ENTRADA.getDesc())));
			assertFalse(result.stream().anyMatch(m -> m.getTipo().equals(TipoMovimentacao.SAIDA.getDesc())));
			assertTrue(result.stream().allMatch(m -> m.getDocumento().toLowerCase().contains("notafiscal")));
		});
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindParamiterizadoFornecedor() throws Exception {
		preparaTestParameterizado();

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("fornecedores", bd.forf1.getId().toString());

		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 1, 1).andDo(result -> {
			RestResponsePage<MovimentacaoListDTO> responseList = getPage(result);
			assertTrue(responseList.getContent().stream().allMatch(p -> p.getId().equals(bd.ent2.getId())));

		});

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindParamiterizadoProdutos() throws Exception {
		preparaTestParameterizado();

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("produtos", bd.p4.getId().toString());
		params.add("produtos", bd.p5.getId().toString());

		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 4, 1).andDo(result -> {
			RestResponsePage<MovimentacaoListDTO> responseList = getPage(result);
			assertTrue(responseList.stream().allMatch(m -> Arrays.asList(bd.ent2, bd.sai2, bd.ent3, bd.sai3).stream()
					.map(mo -> mo.getId()).anyMatch(i -> i.equals(m.getId()))));

		});

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindParamiterizadoProdutoAndTipo() throws Exception {
		preparaTestParameterizado();

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("tipo", TipoMovimentacao.ENTRADA.getDesc());
		params.add("produtos", bd.p4.getId().toString());
		params.add("produtos", bd.p5.getId().toString());

		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 2, 1).andDo(result -> {
			RestResponsePage<MovimentacaoListDTO> responseList = getPage(result);
			assertTrue(responseList.stream().allMatch(m -> Arrays.asList(bd.ent2, bd.ent3).stream()
					.map(mo -> mo.getId()).anyMatch(i -> i.equals(m.getId()))));

		});

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindParamiterizadoAllNull() throws Exception {
		preparaTestParameterizado();

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 6, 1).andDo(result -> {
			RestResponsePage<MovimentacaoListDTO> responseList = getPage(result);
			assertTrue(responseList.stream()
					.allMatch(m -> Arrays.asList(bd.ent2, bd.ent3, bd.ent1, bd.sai1, bd.sai2, bd.sai3).stream()
							.map(mo -> mo.getId()).anyMatch(i -> i.equals(m.getId()))));

		});

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GETd" })
	public void testFindParamiterizadoSemPermissao() throws Exception {
		preparaTestParameterizado();

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

		util.testGetRequestParams(ENDPOINT_BASE + "/page", params, status().isForbidden());
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "DELETE" })
	public void testDeleteSucesso() throws Exception {
		bd.getMovimentacaoS().save(bd.ent1);
		util.testDelete(ENDPOINT_BASE + "/" + bd.ent1.getId(), status().isNoContent());
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "DELETE" })
	public void testDeleteNotFound() throws Exception {

		util.testDelete(ENDPOINT_BASE + "/1", status().isNotFound());
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "DELETe" })
	public void testDeleteSemPermissao() throws Exception {

		util.testDelete(ENDPOINT_BASE + "/1", status().isForbidden());
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "DELETe" })
	public void testDeleteDataIntegrition() throws Exception {

		util.testDelete(ENDPOINT_BASE + "/null", status().isBadRequest());
	}

}
