package com.ebm.todosteste;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
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

public class FuncionarioResourceTest extends BaseTest {
	@Autowired
	private PopulaBD bd;

	private final String ENDPOINT_BASE = "/funcionarios";
	private final String BASE_AUTHORITY = "FUNCIONARIO_";

	@Before
	public void setUp() {
		this.bd.instanciaFuncionario(true);
		this.bd.getCargoS().save(this.bd.cAdministrador);
		this.bd.getCargoS().save(this.bd.cDesenvolvedor);
		this.bd.getPessoaS().save(this.bd.pf1);
		this.bd.getPessoaS().save(this.bd.pj1);
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testInsercaoAllNulo() throws Exception {
		this.bd.funf1.setPessoa(null);
		this.bd.funf1.setCargo(null);
		this.bd.funf1.setDataDeAdmissao(null);
		this.bd.funf1.setComissao(null);
		this.bd.funf1.setAdicionalPessoal(null);

		this.util.testPost(ENDPOINT_BASE, this.bd.funf1, status().isUnprocessableEntity()).andDo(print())
				.andDo(resultRequest -> {
					ValidationError error = util.getValidationErrorOf(resultRequest);
					assertTrue(error.getErrors().size() == 5);
					error.getErrors().stream()
							.allMatch(err -> Arrays
									.asList("pessoa", "cargo", "dataDeAdmissao", "comissao", "adicionalPessoal")
									.stream().anyMatch(fi -> err.getFieldName().equals(fi)));
				});
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testValidacaoFuncionarioMinLengths() throws Exception {
		bd.funf1.setMatricula(Utils.getRandomString(2));
		bd.funf1.setComissao(-0.1);
		bd.funf1.setAdicionalPessoal(BigDecimal.valueOf(-0.1));

		util.testPost(ENDPOINT_BASE, bd.funf1, status().isUnprocessableEntity()).andDo(requestResult -> {
			ValidationError errors = util.getValidationErrorOf(requestResult);
			assertTrue(errors.getErrors().size() == 3);
			errors.getErrors().stream().allMatch(err -> Arrays.asList("matricula", "adicionalPessoal", "comissao")
					.stream().anyMatch(fi -> err.getFieldName().equals(fi)));
		});

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testValidacaoInsercao() throws Exception {
		bd.funf1.setMatricula(Utils.getRandomString(3));
		bd.funf1.setComissao(0d);
		bd.funf1.setAdicionalPessoal(BigDecimal.valueOf(0));

		util.testPostExpectCreated(ENDPOINT_BASE, bd.funf1);

		bd.funf1 = this.bd.getFuncionarioS().findById(this.bd.pf1.getId());

		assertTrue(bd.funf1.getCargo().getNomeCargo() != null);

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testValidacaoInsercaoAssociacoesTendoApenasId() throws Exception {
		Cargo c = new Cargo(bd.funf1.getCargo().getId(), null, null, null);
		PessoaFisica pf = new PessoaFisica(this.bd.pf1.getId(), null, null, null, null, null, null);
		this.bd.funf1.setCargo(c);
		this.bd.funf1.setPessoa(pf);

		util.testPostExpectCreated(ENDPOINT_BASE, bd.funf1);
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testValidacaoFuncionarioMaxLengths() throws Exception {
		bd.funf1.setMatricula(Utils.getRandomString(61));
		bd.funf1.setComissao(1.1);

		util.testPost(ENDPOINT_BASE, bd.funf1, status().isUnprocessableEntity()).andDo(requestResult -> {
			ValidationError errors = util.getValidationErrorOf(requestResult);
			assertTrue(errors.getErrors().size() == 2);
			errors.getErrors().stream().allMatch(err -> Arrays.asList("matricula", "comissao").stream()
					.anyMatch(fi -> err.getFieldName().equals(fi)));
		});

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testInsersaoComMatriculaReptida() throws Exception {
		this.bd.getFuncionarioS().save(bd.funf1);
		bd.funj1.setMatricula(bd.funf1.getMatricula());

		this.util.testPost(ENDPOINT_BASE, this.bd.funj1, status().isBadRequest());
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POSsT" })
	public void testInsersaoSemAthority() throws Exception {

		this.util.testPostExpectForbidden(ENDPOINT_BASE, this.bd.funj1);
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testInsercaoClienteComPessoaQueJaPertenceAOutroCliente() throws Exception {
		bd.funf1.setPessoa(bd.funj1.getPessoa());
		bd.funf1 = this.bd.getFuncionarioS().save(bd.funf1);
		this.util.testPost(ENDPOINT_BASE, this.bd.funj1, status().isBadRequest());
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "PUT",
			BASE_AUTHORITY + "POST" })
	public void testUpdateSemMudarPessoa() throws Exception {

		this.util.testPostExpectCreated(ENDPOINT_BASE, this.bd.funf1).andDo(result -> {
			Integer id = this.util.getIdRedirect(result, ENDPOINT_BASE);
			this.bd.funf1.setId(id);
			bd.funf1.setCargo(bd.cAdministrador);
			bd.funf1.getPessoa().setNome("novonome");
			this.util.testPutExpectNoContent(ENDPOINT_BASE + "/" + id, this.bd.funf1);

			bd.funf1 = this.bd.getFuncionarioS().findById(this.bd.funf1.getId());
			assertThat(bd.funf1.getCargo(), equalTo(bd.cAdministrador));
			assertThat(bd.funf1.getPessoa().getNome(), equalTo("novonome"));

		});
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "PUT",
			BASE_AUTHORITY + "POST" })
	public void testUpdateMudarPessoa() throws Exception {

		this.util.testPostExpectCreated(ENDPOINT_BASE, this.bd.funf1).andDo(result -> {
			Integer id = this.util.getIdRedirect(result, ENDPOINT_BASE);
			this.bd.funf1.setId(id);
			bd.funf1.setPessoa(bd.pj1);

			this.util.testPut(ENDPOINT_BASE + "/" + id, this.bd.funf1, status().isBadRequest());

		});
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "PUT",
			BASE_AUTHORITY + "POST" })
	public void testUpdateClienteComPessoaPertenceOutroCliente() throws Exception {
		bd.funj1 = this.bd.getFuncionarioS().save(bd.funj1);

		bd.funf1.setPessoa(bd.funj1.getPessoa());

		this.util.testPost(ENDPOINT_BASE, this.bd.funf1, status().isBadRequest());

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testaFindById() throws Exception {
		this.bd.getFuncionarioS().save(bd.funj1);
		util.testGetExpectedSucess(this.ENDPOINT_BASE, bd.funj1.getId());
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
		this.bd.getFuncionarioS().save(bd.funj1);
		util.testGet(this.ENDPOINT_BASE, bd.funj1.getId(), status().isForbidden());
	}

	//
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindCnpj() throws Exception {
		this.bd.getFuncionarioS().save(bd.funj1);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("value", this.bd.funj1.getPessoa().getDocument());

		this.util.testGetRequestParams(ENDPOINT_BASE + "/document", params, status().isOk()).andDo(result -> {
			Funcionario func_resut = this.util.objectMapper().readValue(result.getResponse().getContentAsString(),
					Funcionario.class);
			assertTrue(func_resut.getId().equals(this.bd.funj1.getPessoa().getId()));
		});
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindCnpjInvalido() throws Exception {
		this.bd.getFuncionarioS().save(bd.funj1);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("value", this.bd.funj1.getPessoa().getDocument() + "1");

		this.util.testGetRequestParams(ENDPOINT_BASE + "/document", params, status().isBadRequest());
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindCnpjNaoExiste() throws Exception {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("value", this.bd.funj1.getPessoa().getDocument());

		this.util.testGetRequestParams(ENDPOINT_BASE + "/document", params, status().isNotFound());
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindCPF() throws Exception {
		this.bd.getFuncionarioS().save(bd.funf1);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("value", this.bd.funf1.getPessoa().getDocument());

		this.util.testGetRequestParams(ENDPOINT_BASE + "/document", params, status().isOk()).andDo(result -> {
			Funcionario func_resut = this.util.objectMapper().readValue(result.getResponse().getContentAsString(),
					Funcionario.class);
			assertTrue(func_resut.getId().equals(this.bd.funf1.getPessoa().getId()));
		});
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GeET" })
	public void testFindCPFSemPermissao() throws Exception {
		this.bd.getFuncionarioS().save(bd.funf1);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("value", this.bd.funf1.getPessoa().getDocument());

		this.util.testGetRequestParams(ENDPOINT_BASE + "/document", params, status().isForbidden());
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindCPFInvalido() throws Exception {
		this.bd.getFuncionarioS().save(bd.funf1);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("value", this.bd.funf1.getPessoa().getDocument() + "1");

		this.util.testGetRequestParams(ENDPOINT_BASE + "/document", params, status().isBadRequest());
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testFindCPFNaoExiste() throws Exception {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("value", this.bd.funf1.getPessoa().getDocument());

		this.util.testGetRequestParams(ENDPOINT_BASE + "/document", params, status().isNotFound());
	}

	private RestResponsePage<FuncionarioListDTO> getPage(MvcResult result)
			throws UnsupportedEncodingException, IOException, JsonParseException, JsonMappingException {
		String content = result.getResponse().getContentAsString();
		RestResponsePage<FuncionarioListDTO> responseList = this.util.objectMapper().readValue(content,
				new TypeReference<RestResponsePage<FuncionarioListDTO>>() {
				});
		return responseList;
	}

	private void cenarioParaBuscaParamiterizada() {
		this.bd.getPessoaS().saveAll(Arrays.asList(bd.pf2, bd.pf3, bd.pf4, bd.pj2, bd.pj3, bd.pj4));
		this.bd.getFuncionarioS()
				.saveAll(Arrays.asList(bd.funf1, bd.funf2, bd.funf3, bd.funf4, bd.funj1, bd.funj2, bd.funj3, bd.funj4));

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testaBuscaParamiterizadaPessoaFisica() throws Exception {
		cenarioParaBuscaParamiterizada();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("tipo", TipoPessoa.PESSOA_FISICA.getDescricao());

		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 4, 1).andDo(result_ -> {
			RestResponsePage<FuncionarioListDTO> result = this.getPage(result_);

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
			RestResponsePage<FuncionarioListDTO> result = this.getPage(result_);

			assertTrue(result.get().allMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_JURIDICA.getDescricao())));
			assertFalse(result.get().anyMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_FISICA.getDescricao())));
		});

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testaBuscaParamiterizadaAllNull() throws Exception {
		cenarioParaBuscaParamiterizada();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 8, 1).andDo(result_ -> {
			RestResponsePage<FuncionarioListDTO> result = this.getPage(result_);

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
			RestResponsePage<FuncionarioListDTO> result = this.getPage(result_);

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
			RestResponsePage<FuncionarioListDTO> result = this.getPage(result_);

			assertTrue(result.get().allMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_FISICA.getDescricao())));
			assertTrue(result.get().allMatch(c -> c.getNome().toLowerCase().contains("joao")));
		});
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testaBuscaParamiterizadaCargo() throws Exception {
		cenarioParaBuscaParamiterizada();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("cargo", this.bd.cAdministrador.getNomeCargo());
		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 5, 1).andDo(result_ -> {
			RestResponsePage<FuncionarioListDTO> result = this.getPage(result_);

			assertTrue(result.get().allMatch(f -> f.getCargo().equals(bd.cAdministrador.getNomeCargo())));
			assertFalse(result.get().anyMatch(f -> f.getCargo().equals(bd.cDesenvolvedor.getNomeCargo())));
		});
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testaBuscaParamiterizadaCargoETipo() throws Exception {
		cenarioParaBuscaParamiterizada();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("cargo", this.bd.cAdministrador.getNomeCargo());
		params.add("tipo", TipoPessoa.PESSOA_FISICA.getDescricao());

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testaBuscaParamiterizadaCargoENome() throws Exception {
		cenarioParaBuscaParamiterizada();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("cargo", this.bd.cAdministrador.getNomeCargo());
		params.add("nome", "MaRiA");

		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 1, 1).andDo(result_ -> {
			RestResponsePage<FuncionarioListDTO> result = this.getPage(result_);
			assertTrue(result.get().allMatch(f -> f.getCargo().equals(bd.cAdministrador.getNomeCargo())));
			assertTrue(result.get().allMatch(f -> f.getNome().toLowerCase().contains("maria")));
		});
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testeBuscarMatricula() throws Exception {
		cenarioParaBuscaParamiterizada();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("matricula", "adm");

		util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 4, 1).andDo(result_ -> {
			RestResponsePage<FuncionarioListDTO> result = this.getPage(result_);
			assertTrue(result.get().allMatch(f -> f.getMatricula().toLowerCase().contains("adm")));

		});
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "DELETE" })
	public void testDeleteSucesso() throws Exception {
		this.bd.getFuncionarioS().save(bd.funj1);

		util.testDelete(ENDPOINT_BASE + "/" + bd.funj1.getId(), status().isNoContent());
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
