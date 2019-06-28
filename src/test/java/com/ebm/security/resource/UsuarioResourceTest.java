package com.ebm.security.resource;

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
import java.util.HashSet;

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
import com.ebm.pessoal.domain.Funcionario;
import com.ebm.pessoal.domain.TipoPessoa;
import com.ebm.pessoal.dtos.ClienteListDTO;
import com.ebm.security.Usuario;
import com.ebm.security.dto.UsuarioNewDTO;
import com.ebm.security.dto.UsuarioUpdateDTO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

public class UsuarioResourceTest extends BaseTest {
	@Autowired
	private PopulaBD bd;

	private final String ENDPOINT_BASE = "/usuarios";
	private final String BASE_AUTHORITY = "USUARIO_";

	@Before
	public void setUp() {
		bd.instanciaFuncionario(true).instanciaUsuarios();
		this.bd.getCargoS().save(bd.funf1.getCargo());
		this.bd.getCargoS().save(bd.funf2.getCargo());
		this.bd.getPessoaS().save(bd.funf1.getPessoa());
		this.bd.getPessoaS().save(bd.funf2.getPessoa());
		this.bd.getFuncionarioS().save(bd.funf1);
		this.bd.getFuncionarioS().save(bd.funf2);
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testInsercaoAllNulo() throws Exception {
		UsuarioNewDTO user = UsuarioNewDTO.from(this.bd.user1);
		user.setLogin(null);
		user.setSenha(null);
		user.setPermissoes(null);
		user.setFuncionario_id(null);
		
		this.util.testPost(ENDPOINT_BASE, user, status().isUnprocessableEntity()).andDo(print())
				.andDo(resultRequest -> {
					ValidationError error = util.getValidationErrorOf(resultRequest);
					assertTrue(error.getErrors().size() == 5);
					error.getErrors().stream().allMatch(err -> Arrays.asList("login", "senha", "permissoes", "funcionario_id").stream()
							.anyMatch(fi -> err.getFieldName().equals(fi)));
				});
	}
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testInsercaoAllEmptyAndLength() throws Exception {
		UsuarioNewDTO user = UsuarioNewDTO.from(this.bd.user1);
		user.setLogin("");
		user.setSenha("");
		user.setPermissoes(new HashSet<>());
		
		this.util.testPost(ENDPOINT_BASE, user, status().isUnprocessableEntity()).andDo(print())
				.andDo(resultRequest -> {
					ValidationError error = util.getValidationErrorOf(resultRequest);
					assertTrue(error.getErrors().size() == 5);
					error.getErrors().stream().allMatch(err -> Arrays.asList("login", "senha", "permissoes").stream()
							.anyMatch(fi -> err.getFieldName().equals(fi)));
				});
	}
	

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testValidacaoInsercao() throws Exception {
		UsuarioNewDTO user = UsuarioNewDTO.from(this.bd.user1);
		util.testPostExpectCreated(ENDPOINT_BASE, user);
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testValidacaoClienteMaxLengths() throws Exception {
		UsuarioNewDTO user = UsuarioNewDTO.from(this.bd.user1);
		user.setLogin(Utils.getRandomString(21));
		user.setSenha(Utils.getRandomString(21));

		util.testPost(ENDPOINT_BASE, user, status().isUnprocessableEntity()).andDo(requestResult -> {
			ValidationError errors = util.getValidationErrorOf(requestResult);
			assertTrue(errors.getErrors().size() == 2);
			errors.getErrors().stream().allMatch(err -> Arrays.asList("login", "senha").stream()
					.anyMatch(fi -> err.getFieldName().equals(fi)));
		});

	}



	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POSsT" })
	public void testInsersaoSemAthority() throws Exception {

		this.util.testPostExpectForbidden(ENDPOINT_BASE, this.bd.cj1);
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testInsercaoUsuarioComPessoaQueJaPertenceAOutroCliente() throws Exception {
		UsuarioNewDTO user1 = UsuarioNewDTO.from(bd.user1);
		UsuarioNewDTO user2 = UsuarioNewDTO.from(bd.user2);
		bd.getUsuarioService().save(user2);
		user1.setFuncionario_id(user2.getFuncionario_id());
		this.util.testPost(ENDPOINT_BASE, user1, status().isBadRequest());
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "PUT",
			BASE_AUTHORITY + "POST" })
	public void testUpdateSemMudarPessoa() throws Exception {
		UsuarioNewDTO user1 = UsuarioNewDTO.from(bd.user1);
		this.bd.user1 = bd.getUsuarioService().save(user1);
		String senha = this.bd.user1.getSenha();
		this.util.em().detach(this.bd.user1);
		this.bd.user1.setLogin("novologin");
		this.bd.user1.setSenha("novasenha");
		UsuarioUpdateDTO user2 = UsuarioUpdateDTO.from(bd.user1);
		this.util.testPutExpectNoContent(ENDPOINT_BASE + "/" + this.bd.user1.getId(), user2);
		this.bd.user1 = this.bd.getUsuarioService().find(this.bd.user1.getId());
		assertThat(this.bd.user1.getLogin() , equalTo("novoLogin"));
		assertThat(this.bd.user1.getSenha(), equalTo(senha));

	
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "PUT",
			BASE_AUTHORITY + "POST" })
	public void testUpdateMudarPessoa() throws Exception {

		this.util.testPostExpectCreated(ENDPOINT_BASE, this.bd.cf1).andDo(result -> {
			Integer id = this.util.getIdRedirect(result, ENDPOINT_BASE);
			this.bd.cf1.setId(id);
			bd.cf1.setPessoa(bd.pj1);

			this.util.testPut(ENDPOINT_BASE + "/" + id, this.bd.cf1, status().isBadRequest());

		});
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "PUT",
			BASE_AUTHORITY + "POST" })
	public void testUpdateClienteComPessoaPertenceOutroCliente() throws Exception {
		bd.cj1 = this.bd.getClientS().save(bd.cj1);

		bd.cf1.setPessoa(bd.cj1.getPessoa());

		this.util.testPost(ENDPOINT_BASE, this.bd.cf1, status().isBadRequest());

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testaFindById() throws Exception {
		this.bd.getClientS().save(bd.cj1);
		util.testGetExpectedSucess(this.ENDPOINT_BASE, bd.cj1.getId());
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
		this.bd.getClientS().save(bd.cj1);
		util.testGet(this.ENDPOINT_BASE, bd.cj1.getId(), status().isForbidden());
	}

	//
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindCnpj() throws Exception {
		this.bd.getClientS().save(bd.cj1);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("value", this.bd.cj1.getPessoa().getDocument());

		this.util.testGetRequestParams(ENDPOINT_BASE + "/document", params, status().isOk()).andDo(result -> {
			Funcionario func_resut = this.util.objectMapper().readValue(result.getResponse().getContentAsString(),
					Funcionario.class);
			assertTrue(func_resut.getId().equals(this.bd.cj1.getPessoa().getId()));
		});
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindCnpjInvalido() throws Exception {
		this.bd.getClientS().save(bd.cj1);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("value", this.bd.cj1.getPessoa().getDocument() + "1");

		this.util.testGetRequestParams(ENDPOINT_BASE + "/document", params, status().isBadRequest());
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindCnpjNaoExiste() throws Exception {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("value", this.bd.cj1.getPessoa().getDocument());

		this.util.testGetRequestParams(ENDPOINT_BASE + "/document", params, status().isNotFound());
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindCPF() throws Exception {
		this.bd.getClientS().save(bd.cf1);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("value", this.bd.cf1.getPessoa().getDocument());

		this.util.testGetRequestParams(ENDPOINT_BASE + "/document", params, status().isOk()).andDo(result -> {
			Funcionario func_resut = this.util.objectMapper().readValue(result.getResponse().getContentAsString(),
					Funcionario.class);
			assertTrue(func_resut.getId().equals(this.bd.cf1.getPessoa().getId()));
		});
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GeET" })
	public void testFindCPFSemPermissao() throws Exception {
		this.bd.getClientS().save(bd.cf1);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("value", this.bd.cf1.getPessoa().getDocument());

		this.util.testGetRequestParams(ENDPOINT_BASE + "/document", params, status().isForbidden());
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindCPFInvalido() throws Exception {
		this.bd.getClientS().save(bd.cf1);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("value", this.bd.cf1.getPessoa().getDocument() + "1");

		this.util.testGetRequestParams(ENDPOINT_BASE + "/document", params, status().isBadRequest());
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindCPFNaoExiste() throws Exception {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("value", this.bd.cf1.getPessoa().getDocument());

		this.util.testGetRequestParams(ENDPOINT_BASE + "/document", params, status().isNotFound());
	}
//
	private RestResponsePage<ClienteListDTO> getPage(MvcResult result)
			throws UnsupportedEncodingException, IOException, JsonParseException, JsonMappingException {
		String content = result.getResponse().getContentAsString();
		RestResponsePage<ClienteListDTO> responseList = this.util.objectMapper().readValue(content,
				new TypeReference<RestResponsePage<ClienteListDTO>>() {
				});
		return responseList;
	}
//
	private void cenarioParaBuscaParamiterizada() {
		this.bd.getPessoaS().saveAll(Arrays.asList(bd.pf2, bd.pf3, bd.pf4, bd.pj2, bd.pj3, bd.pj4));
		this.bd.getClientS()
				.saveAll(Arrays.asList(bd.cf1, bd.cf2, bd.cf3, bd.cf4, bd.cj1, bd.cj2, bd.cj3, bd.cj4));

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
			RestResponsePage<ClienteListDTO> result = this.getPage(result_);

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
			RestResponsePage<ClienteListDTO> result = this.getPage(result_);

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
			RestResponsePage<ClienteListDTO> result = this.getPage(result_);

			assertTrue(result.get().anyMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_JURIDICA.getDescricao())));
			assertTrue(result.get().anyMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_FISICA.getDescricao())));
		});

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testaBuscaParamiterizadaNome() throws Exception {
		cenarioParaBuscaParamiterizada();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("nome", "joao");
		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 3, 1).andDo(result_ -> {
			RestResponsePage<ClienteListDTO> result = this.getPage(result_);

			assertTrue(result.get().allMatch(c -> c.getNome().toLowerCase().contains("joao")));
		});
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testaBuscaParamiterizadaNomeETipo() throws Exception {
		cenarioParaBuscaParamiterizada();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("nome", "joao");
		params.add("tipo", TipoPessoa.PESSOA_FISICA.getDescricao());
		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 2, 1).andDo(result_ -> {
			RestResponsePage<ClienteListDTO> result = this.getPage(result_);

			assertTrue(result.get().allMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_FISICA.getDescricao())));
			assertTrue(result.get().allMatch(c -> c.getNome().toLowerCase().contains("joao")));
		});
	}


	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "DELETE" })
	public void testDeleteSucesso() throws Exception {
		this.bd.getClientS().save(bd.cj1);

		util.testDelete(ENDPOINT_BASE + "/" + bd.cj1.getId(), status().isNoContent());
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
