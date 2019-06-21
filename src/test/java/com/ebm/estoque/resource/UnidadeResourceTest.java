package com.ebm.estoque.resource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.BaseTest;
import com.ebm.estoque.domain.Unidade;
import com.ebm.estoque.service.interfaces.UnidadeService;
import com.ebm.geral.service.PopulaBD;

public class UnidadeResourceTest extends BaseTest{


	@Autowired
	private UnidadeService uniServ;
	@Autowired
	private PopulaBD bd;


	private final String ENDPOINT_BASE = "/unidades";
	private final String BASE_AUTHORITY = "ITEM_AUX_";

	@Before
	public void setUp() {
		bd.instanciaUnidade();

	}
	
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testaAdicionarUnidadeDeveAceitar() throws Exception {
		util.testPostExpectCreated(ENDPOINT_BASE, bd.un1);
	}
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY  })
	public void testaAdicionarUnidadeDeveRejeitarCredencial() throws Exception {
		util.testPostExpectForbidden(ENDPOINT_BASE, bd.un1);
	}
	
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testaAdicionarUnidadeObjetoInvalido() throws Exception {
		bd.un1.setAbrev(null);
		util.testPost(ENDPOINT_BASE, bd.un1, status().isUnprocessableEntity());
	}
	
	// aqui deve ficar o teste para ver a validação do nome repetido.
	
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "PUT" })
	public void testeUpdateUnidade() throws Exception {
		bd.un1 = uniServ.save(bd.un1);
		util.em().detach(bd.un1);
		bd.un1.setNome("novonome");
		Unidade find = uniServ.findById(bd.un1.getId());
		
		assertFalse(bd.un1.getNome().equals(find.getNome()));
		
		util.testPutExpectNoContent(ENDPOINT_BASE+"/"+bd.un1.getId(), bd.un1)	;
		find = uniServ.findById(bd.un1.getId());
		assertTrue(bd.un1.getNome().equals(find.getNome()));
	}
	
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY  })
	public void testaUpdateUnidadeDeveRejeitarCredencial() throws Exception {
		util.testPutExpectedForbidden(ENDPOINT_BASE+"/1", bd.un1);
	}
	
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "PUT" })
	public void testaUpdateUnidadeObjetoInvalido() throws Exception {
		bd.un1 = uniServ.save(bd.un1);
		util.em().detach(bd.un1);
		bd.un1.setNome("novonome");
		Unidade find = uniServ.findById(bd.un1.getId());
		assertFalse(bd.un1.getNome().equals(find.getNome()));
		bd.un1.setNome(null);
		util.testPut(ENDPOINT_BASE+"/"+bd.un1.getId(), bd.un1, status().isUnprocessableEntity());
	}
	
	
	// aqui deve ficar o teste para ver a validação do nome repetido.
	
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "DELETE", BASE_AUTHORITY + "GET" })
	public void testDeleteUnidade() throws Exception {
		bd.un1 = uniServ.save(bd.un1);
		util.em().detach(bd.un1);
		util.testDelete(ENDPOINT_BASE+"/"+bd.un1.getId(), status().isNoContent())	;
		util.testGet(ENDPOINT_BASE, bd.un1.getId(), status().isNotFound());
	}
	
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY })
	public void testDeleteUnidadeFalhaCredencial() throws Exception {
		util.testDelete(ENDPOINT_BASE+"/1", status().isForbidden());
	}
	
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "DELETE" })
	public void testDeleteNotFound() throws Exception {	
		util.testDelete(ENDPOINT_BASE+"/1", status().isNotFound());
	}
	
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = {BASE_AUTHORITY + "GET" })
	public void testFindById() throws Exception {
		bd.un1 = uniServ.save(bd.un1);
		util.em().detach(bd.un1);
		util.testGet(ENDPOINT_BASE, bd.un1.getId(), status().isOk());
	}
	
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = {BASE_AUTHORITY + "" })
	public void testFindByIdFalhaCredencial() throws Exception {
		bd.un1 = uniServ.save(bd.un1);
		util.em().detach(bd.un1);
		util.testGet(ENDPOINT_BASE, bd.un1.getId(), status().isForbidden());
	}
	
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = {BASE_AUTHORITY + "GET" })
	public void testFindByIdNotFound() throws Exception {
		util.testGet(ENDPOINT_BASE, 1, status().isNotFound());
		
	}
	
}
