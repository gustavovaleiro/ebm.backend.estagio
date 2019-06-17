package com.ebm.estoque.resource;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static  org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.TestUtils;
import com.ebm.estoque.domain.Item;
import com.ebm.estoque.domain.Produto;
import com.ebm.estoque.service.interfaces.CategoriaItemService;
import com.ebm.estoque.service.interfaces.ItemService;
import com.ebm.estoque.service.interfaces.UnidadeService;
import com.ebm.geral.service.PopulaBD;

@ActiveProfiles("testauto")
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ItemResourceTest {

	@Autowired
	private ItemService itemService;
	@Autowired
	private CategoriaItemService catServ;
	@Autowired
	private UnidadeService uniServ;
	@Autowired
	private PopulaBD bd;

	@Autowired
	private TestUtils util;
	
	private final String ENDPOINT_BASE = "itens";
	
	@Before
	public void setUp() {

		bd.instanciaItem(true);
		catServ.saveAll(Arrays.asList(bd.cat1, bd.cat2));
		uniServ.save(bd.un1);
		uniServ.save(bd.un2);
	}

	@Transactional
	@Test
	@WithMockUser(username="test", password="test", authorities = { "ITEM_POST" })
	public void testaInsercaoNormal() throws Exception {
		util.testPostExpectCreated(ENDPOINT_BASE, bd.p1);
	}
	
	@Transactional
	@Test
	@WithMockUser(username="test", password="test", authorities = { "ITEM_POST" })
	public void testaInsercaoNormalErrosObjectNullGiveErrors() throws Exception {
		Produto p = new Produto();
		util.testPost(ENDPOINT_BASE, p, status().isUnprocessableEntity())
				.andExpect(jsonPath("$.errors.*",hasSize(8)));	
	}
	
	@Transactional
	@Test
	@WithMockUser(username="test", password="test", authorities = { "ITEM_GET" })
	public void testInsercaoSemAuthority() throws Exception {
		util.testPost("itens", bd.p1, status().isForbidden());
		
	}


	@Transactional
	@Test
	@WithMockUser(username="test", password="test", authorities = { "ITEM_POST" })
	public void testaInsercaoServiceNormal() throws Exception {
		util.testPostExpectCreated("itens", bd.s1);
	}

	@Transactional
	@Test
	@WithMockUser(username="test", password="test", authorities = {"ITEM_PUT"})
	public void testaUpdate() throws Exception {
		bd.p1 = (Produto) itemService.save(bd.p1);
		util.em().detach(bd.p1);
		bd.p1.setNome("NOVONOME");
		
		util.testUpdateExpectSucess(ENDPOINT_BASE+"/"+bd.p1.getId(), bd.p1);
		
		bd.p1 = (Produto) itemService.findById(bd.p1.getId());
		
		assertThat(bd.p1.getNome(), equalTo("NOVONOME"));
	}
	@Transactional
	@Test
	@WithMockUser(username="test", password="test", authorities = {"ITEM_POST"})
	public void testaUpdateNoAuthority() throws Exception {
		bd.p1 = (Produto) itemService.save(bd.p1);
		util.em().detach(bd.p1);
		bd.p1.setNome("NOVONOME");
		util.testUpdateExpectedForbidden(ENDPOINT_BASE+"/"+bd.p1.getId(), bd.p1);
	}
	

//
//	private void preparaTestParameterizado() {
//		catServ.save(bd.cat3);
//		itemService.saveAll(Arrays.asList(bd.p1, bd.p2, bd.p3, bd.p4, bd.s1, bd.s2, bd.s3, bd.s4));
//
//	}
//
//	@Transactional
//	@Test
//	public void testaFindById() {
//		itemService.save(bd.p1);
//		Produto result = (Produto) itemService.findById(bd.p1.getId());
//
//		assertThat(result.getId(), equalTo(bd.p1.getId()));
//		assertThat(result.getNome(), equalTo(bd.p1.getNome()));
//
//	}
//
//	@Transactional
//	@Test
//	public void testaFindByIdEx() {
//		try {
//			itemService.findById(4);
//			fail();
//		} catch (ObjectNotFoundException ex) {
//			assertThat(ex.getMessage(), equalTo(ItemService.ONFE_BYID));
//		}
//	}
//
//	@Transactional
//	@Test
//	public void testaFindByIdExNUll() {
//		try {
//			itemService.findById(null);
//			fail();
//		} catch (DataIntegrityException ex) {
//
//			assertThat(ex.getMessage(), equalTo(ItemService.DATAINTEGRITY_IDNULL));
//		}
//	}
//
//	@Transactional
//	@Test
//	public void testFindParamiterizadoTipoProduto() {
//		preparaTestParameterizado();
//
//		Page<ItemListDTO> results = itemService.findBy(null, TipoItem.PRODUTO.getDescricao(), null, null, null,
//				PageRequest.of(0, 8));
//
//		assertThat(results.getNumberOfElements(), equalTo(4));
//		assertTrue(results.stream().allMatch(i -> i.getTipo() == TipoItem.PRODUTO.getDescricao()));
//		assertFalse(results.stream().anyMatch(i -> i.getTipo() == TipoItem.SERVICO.getDescricao()));
//
//	}
//
//	@Transactional
//	@Test
//	public void testFindParamiterizadoTipoServico() {
//		preparaTestParameterizado();
//
//		Page<ItemListDTO> results = itemService.findBy(null, TipoItem.SERVICO.getDescricao(), null, null, null,
//				PageRequest.of(0, 8));
//
//		assertThat(results.getNumberOfElements(), equalTo(4));
//		assertTrue(results.stream().allMatch(i -> i.getTipo() == TipoItem.SERVICO.getDescricao()));
//		assertFalse(results.stream().anyMatch(i -> i.getTipo() == TipoItem.PRODUTO.getDescricao()));
//	}
//
//	@Transactional
//	@Test
//	public void testFindParamiterizadoTipoNull() {
//		preparaTestParameterizado();
//
//		Page<ItemListDTO> results = itemService.findBy(null, null, null, null, null, PageRequest.of(0, 8));
//
//		assertThat(results.getNumberOfElements(), equalTo(8));
//		assertTrue(results.stream().anyMatch(i -> i.getTipo() == TipoItem.SERVICO.getDescricao()));
//		assertTrue(results.stream().anyMatch(i -> i.getTipo() == TipoItem.PRODUTO.getDescricao()));
//	}
//
//	// test find parameterizado bd.unidade bd.s1 s3
//	@Transactional
//	@Test
//	public void testFindParameterizadoComUnidade() {
//		preparaTestParameterizado();
//
//		Page<ItemListDTO> results = itemService.findBy(null, null, null, bd.un2.getAbrev(), null, PageRequest.of(0, 8));
//
//		assertThat(results.getNumberOfElements(), equalTo(2));
//		assertTrue(results.stream().anyMatch(i -> i.getNome().equals(bd.s1.getNome())));
//		assertTrue(results.stream().anyMatch(i -> i.getNome().equals(bd.s3.getNome())));
//
//	}
//
//	// tes find parameterizado com nome
//	@Transactional
//	@Test
//	public void testFindParameterizadoComNome() {
//		preparaTestParameterizado();
//
//		Page<ItemListDTO> results = itemService.findBy(null, null, "TeCLAdo", null, null, PageRequest.of(0, 8));
//
//		assertThat(results.getNumberOfElements(), equalTo(2));
//		assertTrue(results.stream().anyMatch(i -> i.getNome().equals(bd.p2.getNome())));
//		assertTrue(results.stream().anyMatch(i -> i.getNome().equals(bd.p3.getNome())));
//
//	}
//
//	// test find parameterizado com bd.categoria
//	@Transactional
//	@Test
//	public void testFindParameterizadoComCategoria() {
//		preparaTestParameterizado();
//
//		Page<ItemListDTO> results = itemService.findBy(null, null, null, null, bd.cat3.getNome(), PageRequest.of(0, 8));
//
//		assertThat(results.getNumberOfElements(), equalTo(3));
//		assertTrue(results.get().allMatch(i -> i.getCategoria().equals(bd.cat3.getNome())));
//
//	}
//
//	// test find parameterizado cod interno
//	@Transactional
//	@Test
//	public void testFindParameterizadoCodInterno() {
//		preparaTestParameterizado();
//
//		Page<ItemListDTO> results = itemService.findBy("computador", null, null, null, null, PageRequest.of(0, 8));
//
//		assertThat(results.getNumberOfElements(), equalTo(2));
//		assertTrue(results.get().allMatch(i -> i.getCodInterno().toLowerCase().contains("computador")));
//
//	}
//
//	// test find parameterizado com bd.unidade e bd.categoria
//	@Transactional
//	@Test
//	public void testFindParameterizadoUnidadeECategoria() {
//		preparaTestParameterizado();
//
//		Page<ItemListDTO> results = itemService.findBy(null, null, null, bd.un1.getAbrev(), bd.cat2.getNome(),
//				PageRequest.of(0, 8));
//
//		assertThat(results.getNumberOfElements(), equalTo(2));
//		assertTrue(results.get().allMatch(i -> i.getUnidade().equals(bd.un1.getAbrev())));
//		assertTrue(results.get().allMatch(i -> i.getCategoria().equals(bd.cat2.getNome())));
//
//	}
//
//	// test find parameterizado com tudo s4 = Servico.of("Formatacao", bd.un1,
//	// bd.cat2);
//	@Transactional
//	@Test
//	public void testFindParameterizadoTodosOsParametros() {
//		preparaTestParameterizado();
//
//		Page<ItemListDTO> results = itemService.findBy(null, TipoItem.SERVICO.getDescricao(), "FoRmAtAcao",
//				bd.un1.getAbrev(), bd.cat2.getNome(), PageRequest.of(0, 8));
//
//		assertThat(results.getNumberOfElements(), equalTo(1));
//		assertTrue(results.get().allMatch(i -> i.getUnidade().equals(bd.un1.getAbrev())));
//		assertTrue(results.get().allMatch(i -> i.getCategoria().equals(bd.cat2.getNome())));
//
//	}

	
}
