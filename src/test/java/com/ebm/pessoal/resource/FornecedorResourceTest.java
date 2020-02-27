package com.ebm.pessoal.resource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.ebm.BaseTest;
import com.ebm.geral.domain.RestResponsePage;
import com.ebm.geral.resource.exception.ValidationError;
import com.ebm.geral.service.PopulaBD;
import com.ebm.pessoal.domain.Funcionario;
import com.ebm.pessoal.domain.TipoPessoa;
import com.ebm.pessoal.dtos.FornecedorListDTO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

public class FornecedorResourceTest extends BaseTest {
	@Autowired
	private PopulaBD bd;

	private final String ENDPOINT_BASE = "/fornecedores";
	private final String BASE_AUTHORITY = "FORNECEDOR_";

	@Before
	public void setUp() {
		this.bd.instanciaCategorias();
		this.bd.getCategoriaS().saveAll(Arrays.asList(this.bd.cat1, this.bd.cat2, this.bd.cat3, this.bd.cat4));
		this.bd.instanciaPessoa().associaPessoa().instanciaFornecedores(false);
		this.bd.getPessoaS().save(this.bd.pf1);
		this.bd.getPessoaS().save(this.bd.pj1);
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testInsercaoAllNulo() throws Exception {
		this.bd.forf1.setPessoa(null);
		this.bd.forf1.setCategorias(null);

		this.util.testPost(ENDPOINT_BASE, this.bd.forf1, status().isUnprocessableEntity()).andDo(print())
				.andDo(resultRequest -> {
					ValidationError error = util.getValidationErrorOf(resultRequest);
					assertTrue(error.getErrors().size() == 1);
					error.getErrors().stream().allMatch(
							err -> Arrays.asList("pessoa").stream().anyMatch(fi -> err.getFieldName().equals(fi)));
				});
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testValidacaoInsercao() throws Exception {
		util.em().detach(bd.forf1.getPessoa());
		bd.forf1.getCategorias().forEach(c -> util.em().detach(c));
		bd.forf1.getPessoa().setNome(null);
		bd.forf1.getCategorias().forEach(c -> c.setNome(null));
		util.testPostExpectCreated(ENDPOINT_BASE, bd.forf1);

		assertNull(bd.forf1.getPessoa().getNome());
		bd.forf1.getCategorias().forEach(c -> assertNull(c.getNome()));
		bd.forf1 = this.bd.getFornecedorS().findById(bd.forf1.getPessoa().getId());
		assertNotNull(bd.forf1.getPessoa().getNome());
		bd.forf1.getCategorias().forEach(c -> assertNotNull(c.getNome()));
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POSsT" })
	public void testInsersaoSemAthority() throws Exception {

		this.util.testPostExpectForbidden(ENDPOINT_BASE, this.bd.forj1);
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testInsercaoFornecedoreComPessoaQueJaPertenceAOutroFornecedore() throws Exception {
		bd.forf1.setPessoa(bd.forj1.getPessoa());
		bd.forf1 = this.bd.getFornecedorS().save(bd.forf1);
		this.util.testPost(ENDPOINT_BASE, this.bd.forj1, status().isBadRequest());
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "PUT",
			BASE_AUTHORITY + "POST" })
	public void testUpdateSemMudarPessoa() throws Exception {

		this.util.testPostExpectCreated(ENDPOINT_BASE, this.bd.forf1).andDo(result -> {
			Integer id = this.util.getIdRedirect(result, ENDPOINT_BASE);
			this.bd.forf1.setId(id);
			bd.forf1.getPessoa().setNome("novonome");
			this.util.testPutExpectNoContent(ENDPOINT_BASE + "/" + id, this.bd.forf1);

			bd.forf1 = this.bd.getFornecedorS().findById(this.bd.forf1.getId());
			assertThat(bd.forf1.getPessoa().getNome(), equalTo("novonome"));

		});
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "PUT",
			BASE_AUTHORITY + "POST" })
	public void testUpdateMudarPessoa() throws Exception {

		this.util.testPostExpectCreated(ENDPOINT_BASE, this.bd.forf1).andDo(result -> {
			Integer id = this.util.getIdRedirect(result, ENDPOINT_BASE);
			this.bd.forf1.setId(id);
			bd.forf1.setPessoa(bd.pj1);

			this.util.testPut(ENDPOINT_BASE + "/" + id, this.bd.forf1, status().isBadRequest());

		});
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "PUT",
			BASE_AUTHORITY + "POST" })
	public void testUpdateFornecedoreComPessoaPertenceOutroFornecedore() throws Exception {
		bd.forj1 = this.bd.getFornecedorS().save(bd.forj1);

		bd.forf1.setPessoa(bd.forj1.getPessoa());

		this.util.testPost(ENDPOINT_BASE, this.bd.forf1, status().isBadRequest());

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testaFindById() throws Exception {
		this.bd.getFornecedorS().save(bd.forj1);
		util.testGetExpectedSucess(this.ENDPOINT_BASE, bd.forj1.getId());
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
		this.bd.getFornecedorS().save(bd.forj1);
		util.testGet(this.ENDPOINT_BASE, bd.forj1.getId(), status().isForbidden());
	}

	//
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindCnpj() throws Exception {
		this.bd.getFornecedorS().save(bd.forj1);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("value", this.bd.forj1.getPessoa().getDocument());

		this.util.testGetRequestParams(ENDPOINT_BASE + "/document", params, status().isOk()).andDo(result -> {
			Funcionario func_resut = this.util.objectMapper().readValue(result.getResponse().getContentAsString(),
					Funcionario.class);
			assertTrue(func_resut.getId().equals(this.bd.forj1.getPessoa().getId()));
		});
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindCnpjInvalido() throws Exception {
		this.bd.getFornecedorS().save(bd.forj1);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("value", this.bd.forj1.getPessoa().getDocument() + "1");

		this.util.testGetRequestParams(ENDPOINT_BASE + "/document", params, status().isBadRequest());
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindCnpjNaoExiste() throws Exception {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("value", this.bd.forj1.getPessoa().getDocument());

		this.util.testGetRequestParams(ENDPOINT_BASE + "/document", params, status().isNotFound());
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindCPF() throws Exception {
		this.bd.getFornecedorS().save(bd.forf1);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("value", this.bd.forf1.getPessoa().getDocument());

		this.util.testGetRequestParams(ENDPOINT_BASE + "/document", params, status().isOk()).andDo(result -> {
			Funcionario func_resut = this.util.objectMapper().readValue(result.getResponse().getContentAsString(),
					Funcionario.class);
			assertTrue(func_resut.getId().equals(this.bd.forf1.getPessoa().getId()));
		});
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GeET" })
	public void testFindCPFSemPermissao() throws Exception {
		this.bd.getFornecedorS().save(bd.forf1);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("value", this.bd.forf1.getPessoa().getDocument());

		this.util.testGetRequestParams(ENDPOINT_BASE + "/document", params, status().isForbidden());
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindCPFInvalido() throws Exception {
		this.bd.getFornecedorS().save(bd.forf1);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("value", this.bd.forf1.getPessoa().getDocument() + "1");

		this.util.testGetRequestParams(ENDPOINT_BASE + "/document", params, status().isBadRequest());
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindCPFNaoExiste() throws Exception {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("value", this.bd.forf1.getPessoa().getDocument());

		this.util.testGetRequestParams(ENDPOINT_BASE + "/document", params, status().isNotFound());
	}

//
	private RestResponsePage<FornecedorListDTO> getPage(MvcResult result)
			throws UnsupportedEncodingException, IOException, JsonParseException, JsonMappingException {
		String content = result.getResponse().getContentAsString();
		RestResponsePage<FornecedorListDTO> responseList = this.util.objectMapper().readValue(content,
				new TypeReference<RestResponsePage<FornecedorListDTO>>() {
				});
		return responseList;
	}

//
	private void cenarioParaBuscaParamiterizada() {
		this.bd.getPessoaS().saveAll(Arrays.asList(bd.pf2, bd.pf3, bd.pf4, bd.pj2, bd.pj3, bd.pj4));
		this.bd.getFornecedorS()
				.saveAll(Arrays.asList(bd.forf1, bd.forf2, bd.forf3, bd.forf4, bd.forj1, bd.forj2, bd.forj3, bd.forj4));

	}

//
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testaBuscaParamiterizadaPessoaFisica() throws Exception {
		cenarioParaBuscaParamiterizada();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("tipo", TipoPessoa.PESSOA_FISICA.getDescricao());

		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 4, 1).andDo(result_ -> {
			RestResponsePage<FornecedorListDTO> result = this.getPage(result_);

			assertTrue(result.get().allMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_FISICA.getDescricao())));
			assertFalse(result.get().anyMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_JURIDICA.getDescricao())));
		});

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testaBuscaParamiterizadaPessoaJuridica() throws Exception {
		cenarioParaBuscaParamiterizada();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("tipo", TipoPessoa.PESSOA_JURIDICA.getDescricao());

		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 4, 1).andDo(result_ -> {
			RestResponsePage<FornecedorListDTO> result = this.getPage(result_);

			assertTrue(result.get().allMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_JURIDICA.getDescricao())));
			assertFalse(result.get().anyMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_FISICA.getDescricao())));
		});

	}

//
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testaBuscaParamiterizadaAllNull() throws Exception {
		cenarioParaBuscaParamiterizada();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 8, 1).andDo(result_ -> {
			RestResponsePage<FornecedorListDTO> result = this.getPage(result_);

			assertTrue(result.get().anyMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_JURIDICA.getDescricao())));
			assertTrue(result.get().anyMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_FISICA.getDescricao())));
		});

	}

	@Transactional
	@Test // bd.forj1 bd.forj3 bd.forj4
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testaBuscaParamiterizadaUmCategoria() throws Exception {

		cenarioParaBuscaParamiterizada();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("categorias", bd.cat1.getId().toString());
		// executa
		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 3, 1).andDo(result_ -> {
			RestResponsePage<FornecedorListDTO> result = getPage(result_);

			assertTrue(result.get().anyMatch(f -> f.getId().equals(bd.forj1.getId())));
			assertTrue(result.get().anyMatch(f -> f.getId() .equals(bd.forj4.getId())));
			assertTrue(result.get().anyMatch(f -> f.getId().equals(bd.forj3.getId())));
		});

	}

	@Transactional
	@Test // bd.forj1
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testaBuscaParamiterizadaDuasCategoria() throws Exception {

		cenarioParaBuscaParamiterizada();

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("categorias", bd.cat3.getId().toString());
		params.add("categorias", bd.cat2.getId().toString());
		// executa
		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 4, 1).andDo(result_ -> {
			RestResponsePage<FornecedorListDTO> result = getPage(result_);

			assertTrue(result.get().anyMatch(f -> f.getId().equals(bd.forf1.getId())));
			assertTrue(result.get().anyMatch(f -> f.getId().equals(bd.forj1.getId())));
			assertTrue(result.get().anyMatch(f -> f.getId() .equals(bd.forf3.getId())));
			assertTrue(result.get().anyMatch(f -> f.getId().equals(bd.forf4.getId())));
		});
	}

	@Transactional
	@Test // bd.forj1
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testaBuscaParamiterizadaCategoriaNome() throws Exception {

		cenarioParaBuscaParamiterizada();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("categorias", bd.cat1.getId().toString());
		params.add("nome", "Joao");
		// executa
		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 1, 1).andDo(result_ -> {
			RestResponsePage<FornecedorListDTO> result = getPage(result_);
			assertTrue(result.get().anyMatch(f -> f.getId().equals(bd.forj3.getId())));

		});

	}

	@Transactional
	@Test // bd.forj1
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testaBuscaParamiterizadaCategoriaTipo() throws Exception {
		this.bd.getPessoaS().save(this.bd.pf5);
		this.bd.getFornecedorS().save(this.bd.forf5);
		cenarioParaBuscaParamiterizada();
		Set<Integer> cats = new HashSet<>(Arrays.asList(bd.cat1.getId()));
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("categorias", bd.cat1.getId().toString());
		params.add("tipo", TipoPessoa.PESSOA_FISICA.getDescricao());
		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 1, 1).andDo(result_ -> {
			RestResponsePage<FornecedorListDTO> result = getPage(result_);
			assertTrue(result.get().anyMatch(f -> f.getId().equals(bd.forf5.getId())));

		});

	}

//
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testaBuscaParamiterizadaNome() throws Exception {
		cenarioParaBuscaParamiterizada();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("nome", "joao");
		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 3, 1).andDo(result_ -> {
			RestResponsePage<FornecedorListDTO> result = this.getPage(result_);

			assertTrue(result.get().allMatch(c -> c.getNome().toLowerCase().contains("joao")));
		});
	}

//
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testaBuscaParamiterizadaNomeETipo() throws Exception {
		cenarioParaBuscaParamiterizada();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("nome", "joao");
		params.add("tipo", TipoPessoa.PESSOA_FISICA.getDescricao());
		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 2, 1).andDo(result_ -> {
			RestResponsePage<FornecedorListDTO> result = this.getPage(result_);

			assertTrue(result.get().allMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_FISICA.getDescricao())));
			assertTrue(result.get().allMatch(c -> c.getNome().toLowerCase().contains("joao")));
		});
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "DELETE" })
	public void testDeleteSucesso() throws Exception {
		this.bd.getFornecedorS().save(bd.forj1);

		util.testDelete(ENDPOINT_BASE + "/" + bd.forj1.getId(), status().isNoContent());
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
