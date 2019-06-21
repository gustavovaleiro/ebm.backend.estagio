package com.ebm.estoque.resource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.BaseTest;
import com.ebm.estoque.domain.Movimentacao;
import com.ebm.estoque.domain.Produto;
import com.ebm.estoque.service.interfaces.CategoriaItemService;
import com.ebm.estoque.service.interfaces.ItemService;
import com.ebm.estoque.service.interfaces.MovimentacaoService;
import com.ebm.estoque.service.interfaces.UnidadeService;
import com.ebm.geral.resource.exception.ValidationError;
import com.ebm.geral.service.PopulaBD;
import com.ebm.geral.utils.Utils;

public class MovimentacaoResourceTest extends BaseTest{

	@Autowired
	private MovimentacaoService movimentacaoService;
	@Autowired
	private UnidadeService unidadeS;
	@Autowired
	private CategoriaItemService categoriaS;
	@Autowired
	private ItemService itemS;
	@Autowired
	private PopulaBD bd;


	private final String ENDPOINT_BASE = "/movimentacoes";
	private final String BASE_AUTHORITY = "MOVIMENTACAO_";
	@Before
	public void setUp() {
		bd.instanciaMovimentacao(true);
	    Arrays.asList(bd.p1,bd.p2,bd.p3)
	    	.forEach( p -> ((Produto) p).setEstoque(5,0,10) );
	    
	    unidadeS.save(bd.un1);
	    unidadeS.save(bd.un1);
	    categoriaS.save(bd.cat1);
	    categoriaS.save(bd.cat2);
	    categoriaS.save(bd.cat3);
	    categoriaS.save(bd.cat4);
	    itemS.save(bd.p1);
	    itemS.save(bd.p2);
	    itemS.save(bd.p3);
	    bd.saveUnidade(Arrays.asList(bd.un1,bd.un2));
	    bd.saveCategoria(Arrays.asList(bd.cat1,bd.cat2,bd.cat3,bd.cat4));
	    bd.saveProduto(Arrays.asList(bd.p1,bd.p2,bd.p3));
	    
	}
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testInserNormal() throws Exception {
		util.testPostExpectCreated(ENDPOINT_BASE, bd.ent1)
			.andDo(result -> {
				Movimentacao mov = movimentacaoService.findById(Integer.valueOf( util.getIdRedirect(result, ENDPOINT_BASE)));
				assertTrue(mov.getProdutos().containsAll(bd.ent1.getProdutos()));
			});
		
		
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
		util.testPost(ENDPOINT_BASE, bd.ent1, status().isUnprocessableEntity()).andExpect(jsonPath("$.errors.*", hasSize(3)));
	}
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testaInsercaoErrosObjectOnlyEmptyGiveErrors() throws Exception {

		bd.ent1.setProdutoMovimentacao(new HashSet<>());
		util.testPost(ENDPOINT_BASE, bd.ent1, status().isUnprocessableEntity()).andExpect(jsonPath("$.errors.*", hasSize(1)));
	}
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testaInsercaoErrosLength() throws Exception {

		bd.ent1.setDescricao(Utils.getRandomString(602));
		bd.ent1.setDocumento(Utils.getRandomString(1));
		util.testPost(ENDPOINT_BASE, bd.ent1, status().isUnprocessableEntity())
			.andDo(result ->{
				ValidationError error = util.getValidationErrorOf(result);
				
				assertTrue(error.getErrors().size() == 2);
				assertTrue(error.getErrors().stream().anyMatch( e -> e.getFieldName().equals("descricao")));
				assertTrue(error.getErrors().stream().anyMatch( e -> e.getFieldName().equals("documento")));
			});
	}


	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testaInsercaoErrosLengthTwo() throws Exception {

		bd.ent1.setDescricao(Utils.getRandomString(444));
		bd.ent1.setDocumento(Utils.getRandomString(42));
		util.testPost(ENDPOINT_BASE, bd.ent1, status().isUnprocessableEntity())
			.andDo(result ->{
				ValidationError error = util.getValidationErrorOf(result);
				
				assertTrue(error.getErrors().size() == 1);
				assertFalse(error.getErrors().stream().anyMatch( e -> e.getFieldName().equals("descricao")));
				assertTrue(error.getErrors().stream().anyMatch( e -> e.getFieldName().equals("documento")));
			});

	}


	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "PUT" })
	public void testaUpdate() throws Exception {
		movimentacaoService.save(bd.ent1);

		bd.ent1.setDescricao("NOVONOME");

		util.testPutExpectNoContent(ENDPOINT_BASE + "/" + bd.ent1.getId(), bd.ent1);

		bd.ent1 =  movimentacaoService.findById(bd.ent1.getId());

		assertTrue(bd.ent1.getDescricao().equals("NOVONOME"));
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testaUpdateNoAuthority() throws Exception {

		util.testPutExpectedForbidden(ENDPOINT_BASE + "/1" , bd.ent1);
	}
//
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
//	public void testaFindById() throws Exception {
//		itemService.save(bd.p1);
//		util.testGetExpectedSucess(this.ENDPOINT_BASE, bd.p1.getId());
//	}
//
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
//	public void testaFindByIdExpectNotFound() throws Exception {
//		util.testGet(this.ENDPOINT_BASE, 1, status().isNotFound());
//	}
//
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GETT" })
//	public void testaFindByIdNoAuthority() throws Exception {
//		itemService.save(bd.p1);
//		util.testGet(this.ENDPOINT_BASE, bd.p1.getId(), status().isForbidden());
//	}
//
//	private void preparaTestParameterizado() {
//		catServ.save(bd.cat3);
//		itemService.saveAll(Arrays.asList(bd.p1, bd.p2, bd.p3, bd.p4, bd.s1, bd.s2, bd.s3, bd.s4));
//
//	}
//
//	private RestResponsePage<ItemListDTO> getPage(MvcResult result)
//			throws UnsupportedEncodingException, IOException, JsonParseException, JsonMappingException {
//		String content = result.getResponse().getContentAsString();
//		RestResponsePage<ItemListDTO> responseList = this.util.objectMapper().readValue(content,
//				new TypeReference<RestResponsePage<ItemListDTO>>() {
//				});
//		return responseList;
//	}
//
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
//	public void testFindParamiterizadoTipoProduto() throws Exception {
//		preparaTestParameterizado();
//
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//		params.add("tipo", TipoItem.PRODUTO.getDescricao());
//
//		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 4, 1).andDo(result -> {
//			RestResponsePage<ItemListDTO> responseList = getPage(result);
//			assertTrue(responseList.getContent().stream()
//					.allMatch(p -> p.getTipo().equalsIgnoreCase(TipoItem.PRODUTO.getDescricao())));
//			assertFalse(responseList.getContent().stream()
//					.anyMatch(p -> p.getTipo().equalsIgnoreCase(TipoItem.SERVICO.getDescricao())));
//		});
//
//	}
//
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
//	public void testFindParamiterizadoTipoServico() throws Exception {
//		preparaTestParameterizado();
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//		params.add("tipo", TipoItem.SERVICO.getDescricao());
//		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 4, 1).andDo(result -> {
//			RestResponsePage<ItemListDTO> results = getPage(result);
//			assertTrue(results.stream().allMatch(i -> i.getTipo().equals(TipoItem.SERVICO.getDescricao())));
//			assertFalse(results.stream().anyMatch(i -> i.getTipo().equals(TipoItem.PRODUTO.getDescricao())));
//		});
//	}
//
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
//	public void testFindParamiterizadoTipoNull() throws Exception {
//		preparaTestParameterizado();
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//
//		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 8, 1).andDo(result -> {
//			RestResponsePage<ItemListDTO> results = getPage(result);
//			assertTrue(results.stream().anyMatch(i -> i.getTipo().equals(TipoItem.SERVICO.getDescricao())));
//			assertTrue(results.stream().anyMatch(i -> i.getTipo().equals(TipoItem.PRODUTO.getDescricao())));
//		}).andDo(print());
//
//	}
//
////	// test find parameterizado bd.unidade bd.s1 s3
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
//	public void testFindParameterizadoComUnidade() throws Exception {
//		preparaTestParameterizado();
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//		params.add("unidade", bd.un2.getAbrev());
//		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 2, 1).andDo(result -> {
//			RestResponsePage<ItemListDTO> results = getPage(result);
//			assertTrue(results.stream().anyMatch(i -> i.getNome().equals(bd.s1.getNome())));
//			assertTrue(results.stream().anyMatch(i -> i.getNome().equals(bd.s3.getNome())));
//		});
//
//	}
//
//	// tes find parameterizado com nome
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
//	public void testFindParameterizadoComNome() throws Exception {
//		preparaTestParameterizado();
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//		params.add("nome", "TeClAdO");
//		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 2, 1).andDo(result -> {
//			RestResponsePage<ItemListDTO> results = getPage(result);
//			assertTrue(results.stream().anyMatch(i -> i.getNome().equals(bd.p2.getNome())));
//			assertTrue(results.stream().anyMatch(i -> i.getNome().equals(bd.p3.getNome())));
//		});
//
//	}
//
//	// test find parameterizado com bd.categoria
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
//	public void testFindParameterizadoComCategoria() throws Exception {
//		preparaTestParameterizado();
//
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//		params.add("categoria", bd.cat3.getNome());
//		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 3, 1).andDo(result -> {
//			RestResponsePage<ItemListDTO> results = getPage(result);
//			assertTrue(results.get().allMatch(i -> i.getCategoria().equals(bd.cat3.getNome())));
//		});
//	}
//
//	// test find parameterizado cod interno
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
//	public void testFindParameterizadoCodInterno() throws Exception {
//		preparaTestParameterizado();
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//		params.add("codInterno", "computador");
//
//		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 2, 1).andDo(result -> {
//			RestResponsePage<ItemListDTO> results = getPage(result);
//			assertTrue(results.get().allMatch(i -> i.getCodInterno().toLowerCase().contains("computador")));
//		});
//
//	}
//
//	// test find parameterizado com bd.unidade e bd.categoria
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
//	public void testFindParameterizadoUnidadeECategoria() throws Exception {
//		preparaTestParameterizado();
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//		params.add("unidade", bd.un1.getAbrev());
//		params.add("categoria", bd.cat2.getNome());
//		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 2, 1).andDo(result -> {
//			RestResponsePage<ItemListDTO> results = getPage(result);
//			assertTrue(results.get().allMatch(i -> i.getUnidade().equals(bd.un1.getAbrev())));
//			assertTrue(results.get().allMatch(i -> i.getCategoria().equals(bd.cat2.getNome())));
//		});
//	}
//
////
////	// test find parameterizado com tudo s4 = Servico.of("Formatacao", bd.un1,
////	// bd.cat2);
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
//	public void testFindParameterizadoTodosOsParametros() throws Exception {
//		preparaTestParameterizado();
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//		params.add("unidade", bd.un1.getAbrev());
//		params.add("categoria", bd.cat2.getNome());
//		params.add("tipo", TipoItem.SERVICO.getDescricao());
//		params.add("nome", "Formatacao");
//
//		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 1, 1).andDo(result -> {
//			RestResponsePage<ItemListDTO> results = getPage(result);
//			assertTrue(results.get().allMatch(i -> i.getUnidade().equals(bd.un1.getAbrev())));
//			assertTrue(results.get().allMatch(i -> i.getCategoria().equals(bd.cat2.getNome())));
//		});
//	}

}
