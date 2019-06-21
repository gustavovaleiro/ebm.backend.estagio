package com.ebm.estoque.resource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.ebm.BaseTest;
import com.ebm.estoque.domain.Produto;
import com.ebm.estoque.domain.enums.TipoItem;
import com.ebm.estoque.dtos.ItemListDTO;
import com.ebm.estoque.service.interfaces.CategoriaItemService;
import com.ebm.estoque.service.interfaces.ItemService;
import com.ebm.estoque.service.interfaces.UnidadeService;
import com.ebm.geral.domain.RestResponsePage;
import com.ebm.geral.service.PopulaBD;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;


public class ItemResourceTest extends BaseTest{

	@Autowired
	private ItemService itemService;
	@Autowired
	private CategoriaItemService catServ;
	@Autowired
	private UnidadeService uniServ;
	@Autowired
	private PopulaBD bd;


	private final String ENDPOINT_BASE = "/itens";
	private final String BASE_AUTHORITY = "ITEM_";

	@Before
	public void setUp() {

		bd.instanciaItem(true);
		catServ.saveAll(Arrays.asList(bd.cat1, bd.cat2));
		uniServ.save(bd.un1);
		uniServ.save(bd.un2);
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testaInsercaoNormal() throws Exception {
		util.testPostExpectCreated(ENDPOINT_BASE, bd.p1);
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testaInsercaoNormalErrosObjectNullGiveErrors() throws Exception {
		Produto p = new Produto();
		util.testPost(ENDPOINT_BASE, p, status().isUnprocessableEntity()).andExpect(jsonPath("$.errors.*", hasSize(8)));
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testInsercaoSemAuthority() throws Exception {
		util.testPost(this.ENDPOINT_BASE, bd.p1, status().isForbidden());

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testaInsercaoServiceNormal() throws Exception {
		util.testPostExpectCreated(this.ENDPOINT_BASE, bd.s1);
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "PUT" })
	public void testaUpdate() throws Exception {
		bd.p1 = (Produto) itemService.save(bd.p1);
		util.em().detach(bd.p1);
		bd.p1.setNome("NOVONOME");

		util.testPutExpectNoContent(ENDPOINT_BASE + "/" + bd.p1.getId(), bd.p1);

		bd.p1 = (Produto) itemService.findById(bd.p1.getId());

		assertThat(bd.p1.getNome(), equalTo("NOVONOME"));
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testaUpdateNoAuthority() throws Exception {
		bd.p1 = (Produto) itemService.save(bd.p1);
		util.em().detach(bd.p1);
		bd.p1.setNome("NOVONOME");
		util.testPutExpectedForbidden(ENDPOINT_BASE + "/" + bd.p1.getId(), bd.p1);
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testaFindById() throws Exception {
		itemService.save(bd.p1);
		util.testGetExpectedSucess(this.ENDPOINT_BASE, bd.p1.getId());
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
		itemService.save(bd.p1);
		util.testGet(this.ENDPOINT_BASE, bd.p1.getId(), status().isForbidden());
	}

	private void preparaTestParameterizado() {
		catServ.save(bd.cat3);
		itemService.saveAll(Arrays.asList(bd.p1, bd.p2, bd.p3, bd.p4, bd.s1, bd.s2, bd.s3, bd.s4));

	}

	private RestResponsePage<ItemListDTO> getPage(MvcResult result)
			throws UnsupportedEncodingException, IOException, JsonParseException, JsonMappingException {
		String content = result.getResponse().getContentAsString();
		RestResponsePage<ItemListDTO> responseList = this.util.objectMapper().readValue(content,
				new TypeReference<RestResponsePage<ItemListDTO>>() {
				});
		return responseList;
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindParamiterizadoTipoProduto() throws Exception {
		preparaTestParameterizado();

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("tipo", TipoItem.PRODUTO.getDescricao());

		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 4, 1).andDo(result -> {
			RestResponsePage<ItemListDTO> responseList = getPage(result);
			assertTrue(responseList.getContent().stream()
					.allMatch(p -> p.getTipo().equalsIgnoreCase(TipoItem.PRODUTO.getDescricao())));
			assertFalse(responseList.getContent().stream()
					.anyMatch(p -> p.getTipo().equalsIgnoreCase(TipoItem.SERVICO.getDescricao())));
		});

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindParamiterizadoTipoServico() throws Exception {
		preparaTestParameterizado();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("tipo", TipoItem.SERVICO.getDescricao());
		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 4, 1).andDo(result -> {
			RestResponsePage<ItemListDTO> results = getPage(result);
			assertTrue(results.stream().allMatch(i -> i.getTipo().equals(TipoItem.SERVICO.getDescricao())));
			assertFalse(results.stream().anyMatch(i -> i.getTipo().equals(TipoItem.PRODUTO.getDescricao())));
		});
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindParamiterizadoTipoNull() throws Exception {
		preparaTestParameterizado();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 8, 1).andDo(result -> {
			RestResponsePage<ItemListDTO> results = getPage(result);
			assertTrue(results.stream().anyMatch(i -> i.getTipo().equals(TipoItem.SERVICO.getDescricao())));
			assertTrue(results.stream().anyMatch(i -> i.getTipo().equals(TipoItem.PRODUTO.getDescricao())));
		}).andDo(print());

	}

//	// test find parameterizado bd.unidade bd.s1 s3
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindParameterizadoComUnidade() throws Exception {
		preparaTestParameterizado();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("unidade", bd.un2.getAbrev());
		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 2, 1).andDo(result -> {
			RestResponsePage<ItemListDTO> results = getPage(result);
			assertTrue(results.stream().anyMatch(i -> i.getNome().equals(bd.s1.getNome())));
			assertTrue(results.stream().anyMatch(i -> i.getNome().equals(bd.s3.getNome())));
		});

	}

	// tes find parameterizado com nome
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindParameterizadoComNome() throws Exception {
		preparaTestParameterizado();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("nome", "TeClAdO");
		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 2, 1).andDo(result -> {
			RestResponsePage<ItemListDTO> results = getPage(result);
			assertTrue(results.stream().anyMatch(i -> i.getNome().equals(bd.p2.getNome())));
			assertTrue(results.stream().anyMatch(i -> i.getNome().equals(bd.p3.getNome())));
		});

	}

	// test find parameterizado com bd.categoria
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindParameterizadoComCategoria() throws Exception {
		preparaTestParameterizado();

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("categoria", bd.cat3.getNome());
		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 3, 1).andDo(result -> {
			RestResponsePage<ItemListDTO> results = getPage(result);
			assertTrue(results.get().allMatch(i -> i.getCategoria().equals(bd.cat3.getNome())));
		});
	}

	// test find parameterizado cod interno
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindParameterizadoCodInterno() throws Exception {
		preparaTestParameterizado();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("codInterno", "computador");

		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 2, 1).andDo(result -> {
			RestResponsePage<ItemListDTO> results = getPage(result);
			assertTrue(results.get().allMatch(i -> i.getCodInterno().toLowerCase().contains("computador")));
		});

	}

	// test find parameterizado com bd.unidade e bd.categoria
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindParameterizadoUnidadeECategoria() throws Exception {
		preparaTestParameterizado();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("unidade", bd.un1.getAbrev());
		params.add("categoria", bd.cat2.getNome());
		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 2, 1).andDo(result -> {
			RestResponsePage<ItemListDTO> results = getPage(result);
			assertTrue(results.get().allMatch(i -> i.getUnidade().equals(bd.un1.getAbrev())));
			assertTrue(results.get().allMatch(i -> i.getCategoria().equals(bd.cat2.getNome())));
		});
	}

//
//	// test find parameterizado com tudo s4 = Servico.of("Formatacao", bd.un1,
//	// bd.cat2);
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindParameterizadoTodosOsParametros() throws Exception {
		preparaTestParameterizado();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("unidade", bd.un1.getAbrev());
		params.add("categoria", bd.cat2.getNome());
		params.add("tipo", TipoItem.SERVICO.getDescricao());
		params.add("nome", "Formatacao");

		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 1, 1).andDo(result -> {
			RestResponsePage<ItemListDTO> results = getPage(result);
			assertTrue(results.get().allMatch(i -> i.getUnidade().equals(bd.un1.getAbrev())));
			assertTrue(results.get().allMatch(i -> i.getCategoria().equals(bd.cat2.getNome())));
		});
	}

}
