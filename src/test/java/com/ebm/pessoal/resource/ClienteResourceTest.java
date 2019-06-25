package com.ebm.pessoal.resource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Arrays;

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
import com.ebm.geral.utils.Utils;
import com.ebm.pessoal.domain.Cargo;
import com.ebm.pessoal.domain.Funcionario;
import com.ebm.pessoal.domain.PessoaFisica;
import com.ebm.pessoal.domain.TipoPessoa;
import com.ebm.pessoal.dtos.FuncionarioListDTO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

public class ClienteResourceTest extends BaseTest {
	@Autowired
	private PopulaBD bd;

	private final String ENDPOINT_BASE = "/clientes";
	private final String BASE_AUTHORITY = "CLIENTE_";

	@Before
	public void setUp() {
		this.bd.instanciaCliente(true);
		this.bd.getPessoaS().save(this.bd.pf1);
		this.bd.getPessoaS().save(this.bd.pj1);
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testInsercaoAllNulo() throws Exception {
		this.bd.cf1.setPessoa(null);
		this.bd.cf1.setLimite_compra(null);

		this.util.testPost(ENDPOINT_BASE, this.bd.cf1, status().isUnprocessableEntity()).andDo(print())
				.andDo(resultRequest -> {
					ValidationError error = util.getValidationErrorOf(resultRequest);
					assertTrue(error.getErrors().size() == 2);
					error.getErrors().stream().allMatch(err -> Arrays.asList("pessoa", "limite_compra").stream()
							.anyMatch(fi -> err.getFieldName().equals(fi)));
				});
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testValidacaoFuncionarioMinLengths() throws Exception {
		bd.cf1.setLimite_compra(BigDecimal.valueOf(-0.1));

		util.testPost(ENDPOINT_BASE, bd.cf1, status().isUnprocessableEntity()).andDo(requestResult -> {
			ValidationError errors = util.getValidationErrorOf(requestResult);
			assertTrue(errors.getErrors().size() == 1);
			errors.getErrors().stream().allMatch(err -> Arrays.asList("limite_compra")
					.stream().anyMatch(fi -> err.getFieldName().equals(fi)));
		});

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testValidacaoInsercao() throws Exception {
		bd.cf1.setDescricao("");
		bd.cf1.setLimite_compra(BigDecimal.valueOf(0));
		bd.cf1.getPessoa().setNome(null);
		util.testPostExpectCreated(ENDPOINT_BASE, bd.cf1);
		
		assertNull(bd.cf1.getPessoa().getNome());
		bd.cf1 = this.bd.getClientS().findById(bd.cf1.getId());
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testValidacaoFuncionarioMaxLengths() throws Exception {
		bd.cf1.setDescricao(Utils.getRandomString(481));
	

		util.testPost(ENDPOINT_BASE, bd.cf1, status().isUnprocessableEntity()).andDo(requestResult -> {
			ValidationError errors = util.getValidationErrorOf(requestResult);
			assertTrue(errors.getErrors().size() == 1);
			errors.getErrors().stream().allMatch(err -> Arrays.asList("descricao").stream()
					.anyMatch(fi -> err.getFieldName().equals(fi)));
		});

	}



	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POSsT" })
	public void testInsersaoSemAthority() throws Exception {

		this.util.testPostExpectForbidden(ENDPOINT_BASE, this.bd.cj1);
	}
//
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
//	public void testInsercaoClienteComPessoaQueJaPertenceAOutroCliente() throws Exception {
//		bd.cf1.setPessoa(bd.cj1.getPessoa());
//		bd.cf1 = this.bd.getFuncionarioS().save(bd.cf1);
//		this.util.testPost(ENDPOINT_BASE, this.bd.cj1, status().isBadRequest());
//	}
//
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "PUT",
//			BASE_AUTHORITY + "POST" })
//	public void testUpdateSemMudarPessoa() throws Exception {
//
//		this.util.testPostExpectCreated(ENDPOINT_BASE, this.bd.cf1).andDo(result -> {
//			Integer id = this.util.getIdRedirect(result, ENDPOINT_BASE);
//			this.bd.cf1.setId(id);
//			bd.cf1.setCargo(bd.cAdministrador);
//			bd.cf1.getPessoa().setNome("novonome");
//			this.util.testPutExpectNoContent(ENDPOINT_BASE + "/" + id, this.bd.cf1);
//
//			bd.cf1 = this.bd.getFuncionarioS().findById(this.bd.cf1.getId());
//			assertThat(bd.cf1.getCargo(), equalTo(bd.cAdministrador));
//			assertThat(bd.cf1.getPessoa().getNome(), equalTo("novonome"));
//
//		});
//	}
//
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "PUT",
//			BASE_AUTHORITY + "POST" })
//	public void testUpdateMudarPessoa() throws Exception {
//
//		this.util.testPostExpectCreated(ENDPOINT_BASE, this.bd.cf1).andDo(result -> {
//			Integer id = this.util.getIdRedirect(result, ENDPOINT_BASE);
//			this.bd.cf1.setId(id);
//			bd.cf1.setPessoa(bd.pj1);
//
//			this.util.testPut(ENDPOINT_BASE + "/" + id, this.bd.cf1, status().isBadRequest());
//
//		});
//	}
//
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "PUT",
//			BASE_AUTHORITY + "POST" })
//	public void testUpdateClienteComPessoaPertenceOutroCliente() throws Exception {
//		bd.cj1 = this.bd.getFuncionarioS().save(bd.cj1);
//
//		bd.cf1.setPessoa(bd.cj1.getPessoa());
//
//		this.util.testPost(ENDPOINT_BASE, this.bd.cf1, status().isBadRequest());
//
//	}
//
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
//	public void testaFindById() throws Exception {
//		this.bd.getFuncionarioS().save(bd.cj1);
//		util.testGetExpectedSucess(this.ENDPOINT_BASE, bd.cj1.getId());
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
//		this.bd.getFuncionarioS().save(bd.cj1);
//		util.testGet(this.ENDPOINT_BASE, bd.cj1.getId(), status().isForbidden());
//	}
//
//	//
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
//	public void testFindCnpj() throws Exception {
//		this.bd.getFuncionarioS().save(bd.cj1);
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//		params.add("value", this.bd.cj1.getPessoa().getDocument());
//
//		this.util.testGetRequestParams(ENDPOINT_BASE + "/document", params, status().isOk()).andDo(result -> {
//			Funcionario func_resut = this.util.objectMapper().readValue(result.getResponse().getContentAsString(),
//					Funcionario.class);
//			assertTrue(func_resut.getId().equals(this.bd.cj1.getPessoa().getId()));
//		});
//	}
//
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
//	public void testFindCnpjInvalido() throws Exception {
//		this.bd.getFuncionarioS().save(bd.cj1);
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//		params.add("value", this.bd.cj1.getPessoa().getDocument() + "1");
//
//		this.util.testGetRequestParams(ENDPOINT_BASE + "/document", params, status().isBadRequest());
//	}
//
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
//	public void testFindCnpjNaoExiste() throws Exception {
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//		params.add("value", this.bd.cj1.getPessoa().getDocument());
//
//		this.util.testGetRequestParams(ENDPOINT_BASE + "/document", params, status().isNotFound());
//	}
//
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
//	public void testFindCPF() throws Exception {
//		this.bd.getFuncionarioS().save(bd.cf1);
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//		params.add("value", this.bd.cf1.getPessoa().getDocument());
//
//		this.util.testGetRequestParams(ENDPOINT_BASE + "/document", params, status().isOk()).andDo(result -> {
//			Funcionario func_resut = this.util.objectMapper().readValue(result.getResponse().getContentAsString(),
//					Funcionario.class);
//			assertTrue(func_resut.getId().equals(this.bd.cf1.getPessoa().getId()));
//		});
//	}
//
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GeET" })
//	public void testFindCPFSemPermissao() throws Exception {
//		this.bd.getFuncionarioS().save(bd.cf1);
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//		params.add("value", this.bd.cf1.getPessoa().getDocument());
//
//		this.util.testGetRequestParams(ENDPOINT_BASE + "/document", params, status().isForbidden());
//	}
//
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
//	public void testFindCPFInvalido() throws Exception {
//		this.bd.getFuncionarioS().save(bd.cf1);
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//		params.add("value", this.bd.cf1.getPessoa().getDocument() + "1");
//
//		this.util.testGetRequestParams(ENDPOINT_BASE + "/document", params, status().isBadRequest());
//	}
//
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
//	public void testFindCPFNaoExiste() throws Exception {
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//		params.add("value", this.bd.cf1.getPessoa().getDocument());
//
//		this.util.testGetRequestParams(ENDPOINT_BASE + "/document", params, status().isNotFound());
//	}
//
//	private RestResponsePage<FuncionarioListDTO> getPage(MvcResult result)
//			throws UnsupportedEncodingException, IOException, JsonParseException, JsonMappingException {
//		String content = result.getResponse().getContentAsString();
//		RestResponsePage<FuncionarioListDTO> responseList = this.util.objectMapper().readValue(content,
//				new TypeReference<RestResponsePage<FuncionarioListDTO>>() {
//				});
//		return responseList;
//	}
//
//	private void cenarioParaBuscaParamiterizada() {
//		this.bd.getPessoaS().saveAll(Arrays.asList(bd.pf2, bd.pf3, bd.pf4, bd.pj2, bd.pj3, bd.pj4));
//		this.bd.getFuncionarioS()
//				.saveAll(Arrays.asList(bd.cf1, bd.funf2, bd.funf3, bd.funf4, bd.cj1, bd.funj2, bd.funj3, bd.funj4));
//
//	}
//
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
//	public void testaBuscaParamiterizadaPessoaFisica() throws Exception {
//		cenarioParaBuscaParamiterizada();
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//		params.add("tipo", TipoPessoa.PESSOA_FISICA.getDescricao());
//
//		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 4, 1).andDo(result_ -> {
//			RestResponsePage<FuncionarioListDTO> result = this.getPage(result_);
//
//			assertTrue(result.get().allMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_FISICA.getDescricao())));
//			assertFalse(result.get().anyMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_JURIDICA.getDescricao())));
//		});
//
//	}
//
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
//	public void testaBuscaParamiterizadaPessoaJuridica() throws Exception {
//		cenarioParaBuscaParamiterizada();
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//		params.add("tipo", TipoPessoa.PESSOA_JURIDICA.getDescricao());
//
//		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 4, 1).andDo(result_ -> {
//			RestResponsePage<FuncionarioListDTO> result = this.getPage(result_);
//
//			assertTrue(result.get().allMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_JURIDICA.getDescricao())));
//			assertFalse(result.get().anyMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_FISICA.getDescricao())));
//		});
//
//	}
//
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
//	public void testaBuscaParamiterizadaAllNull() throws Exception {
//		cenarioParaBuscaParamiterizada();
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//
//		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 8, 1).andDo(result_ -> {
//			RestResponsePage<FuncionarioListDTO> result = this.getPage(result_);
//
//			assertTrue(result.get().anyMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_JURIDICA.getDescricao())));
//			assertTrue(result.get().anyMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_FISICA.getDescricao())));
//		});
//
//	}
//
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
//	public void testaBuscaParamiterizadaNome() throws Exception {
//		cenarioParaBuscaParamiterizada();
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//		params.add("nome", "joao");
//		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 3, 1).andDo(result_ -> {
//			RestResponsePage<FuncionarioListDTO> result = this.getPage(result_);
//
//			assertTrue(result.get().allMatch(c -> c.getNome().toLowerCase().contains("joao")));
//		});
//	}
//
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
//	public void testaBuscaParamiterizadaNomeETipo() throws Exception {
//		cenarioParaBuscaParamiterizada();
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//		params.add("nome", "joao");
//		params.add("tipo", TipoPessoa.PESSOA_FISICA.getDescricao());
//		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 2, 1).andDo(result_ -> {
//			RestResponsePage<FuncionarioListDTO> result = this.getPage(result_);
//
//			assertTrue(result.get().allMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_FISICA.getDescricao())));
//			assertTrue(result.get().allMatch(c -> c.getNome().toLowerCase().contains("joao")));
//		});
//	}
//
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
//	public void testaBuscaParamiterizadaCargo() throws Exception {
//		cenarioParaBuscaParamiterizada();
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//		params.add("cargo", this.bd.cAdministrador.getNomeCargo());
//		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 5, 1).andDo(result_ -> {
//			RestResponsePage<FuncionarioListDTO> result = this.getPage(result_);
//
//			assertTrue(result.get().allMatch(f -> f.getCargo().equals(bd.cAdministrador.getNomeCargo())));
//			assertFalse(result.get().anyMatch(f -> f.getCargo().equals(bd.cDesenvolvedor.getNomeCargo())));
//		});
//	}
//
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
//	public void testaBuscaParamiterizadaCargoETipo() throws Exception {
//		cenarioParaBuscaParamiterizada();
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//		params.add("cargo", this.bd.cAdministrador.getNomeCargo());
//		params.add("tipo", TipoPessoa.PESSOA_FISICA.getDescricao());
//
//	}
//
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
//	public void testaBuscaParamiterizadaCargoENome() throws Exception {
//		cenarioParaBuscaParamiterizada();
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//		params.add("cargo", this.bd.cAdministrador.getNomeCargo());
//		params.add("nome", "MaRiA");
//
//		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 1, 1).andDo(result_ -> {
//			RestResponsePage<FuncionarioListDTO> result = this.getPage(result_);
//			assertTrue(result.get().allMatch(f -> f.getCargo().equals(bd.cAdministrador.getNomeCargo())));
//			assertTrue(result.get().allMatch(f -> f.getNome().toLowerCase().contains("maria")));
//		});
//	}
//
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
//	public void testeBuscarMatricula() throws Exception {
//		cenarioParaBuscaParamiterizada();
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//		params.add("matricula", "adm");
//
//		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 4, 1).andDo(result_ -> {
//			RestResponsePage<FuncionarioListDTO> result = this.getPage(result_);
//			assertTrue(result.get().allMatch(f -> f.getMatricula().toLowerCase().contains("adm")));
//
//		});
//	}
//
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "DELETE" })
//	public void testDeleteSucesso() throws Exception {
//		this.bd.getFuncionarioS().save(bd.cj1);
//
//		util.testDelete(ENDPOINT_BASE + "/" + bd.cj1.getId(), status().isNoContent());
//	}
//
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "DELETE" })
//	public void testDeleteNotFound() throws Exception {
//
//		util.testDelete(ENDPOINT_BASE + "/1", status().isNotFound());
//	}
//
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "DELETe" })
//	public void testDeleteSemPermissao() throws Exception {
//
//		util.testDelete(ENDPOINT_BASE + "/1", status().isForbidden());
//	}
//
//	@Transactional
//	@Test
//	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "DELETe" })
//	public void testDeleteDataIntegrition() throws Exception {
//
//		util.testDelete(ENDPOINT_BASE + "/null", status().isBadRequest());
//	}

}
