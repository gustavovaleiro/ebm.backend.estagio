package com.ebm.pessoal.resource;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
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
import com.ebm.estoque.domain.enums.TipoMovimentacao;
import com.ebm.estoque.dtos.MovimentacaoListDTO;
import com.ebm.geral.domain.RestResponsePage;
import com.ebm.geral.resource.exception.ValidationError;
import com.ebm.geral.service.PopulaBD;
import com.ebm.geral.utils.Utils;
import com.ebm.pessoal.domain.Cargo;
import com.fasterxml.jackson.core.type.TypeReference;

public class CargoResourceTest extends BaseTest {

	@Autowired
	private PopulaBD bd;
	private final String ENDPOINT_BASE = "/cargos";
	private final String BASE_AUTHORITY = "CARGO_";

	@Before
	public void setUp() {
		bd.instanciaCargos();
	}

	private String getS(int i) {
		return Utils.getRandomString(i);
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testInsercaoCargo() throws Exception {

		util.testPostExpectCreated(ENDPOINT_BASE, bd.cAdministrador);
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POdST" })
	public void testInsercaoCargoSemPermissao() throws Exception {

		util.testPost(ENDPOINT_BASE, bd.cAdministrador, status().isForbidden());
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testValidacaoCargoNotNullEEmpty() throws Exception {
		bd.cAdministrador.setNomeCargo(null);
		bd.cAdministrador.setDescricao(null);
		bd.cAdministrador.setSalarioBase(null);

		util.testPost(ENDPOINT_BASE, bd.cAdministrador, status().isUnprocessableEntity()).andDo(requestResult -> {
			ValidationError errors = util.getValidationErrorOf(requestResult);
			assertTrue(errors.getErrors().size() == 3);
			errors.getErrors().stream().allMatch(err -> Arrays.asList("nomeCargo", "salarioBase").stream()
					.anyMatch(fi -> err.getFieldName().equals(fi)));
		});

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testValidacaoCargoStringMinLength() throws Exception {
		bd.cAdministrador.setNomeCargo(getS(2));
		bd.cAdministrador.setDescricao(getS(2));
		bd.cAdministrador.setSalarioBase(BigDecimal.valueOf(-0.1));

		util.testPost(ENDPOINT_BASE, bd.cAdministrador, status().isUnprocessableEntity()).andDo(requestResult -> {
			ValidationError errors = util.getValidationErrorOf(requestResult);
			assertTrue(errors.getErrors().size() == 3);
			errors.getErrors().stream().allMatch(err -> Arrays.asList("nomeCargo", "salarioBase", "descricao").stream()
					.anyMatch(fi -> err.getFieldName().equals(fi)));
		});

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testValidacaoCargoStringCorrectLength() throws Exception {
		bd.cAdministrador.setNomeCargo(getS(60));
		bd.cAdministrador.setDescricao(getS(240));
		bd.cAdministrador.setSalarioBase(BigDecimal.valueOf(0.1));

		util.testPost(ENDPOINT_BASE, bd.cAdministrador, status().isCreated());

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testValidacaoCargoStringMaxLength() throws Exception {
		bd.cAdministrador.setNomeCargo(getS(61));
		bd.cAdministrador.setDescricao(getS(241));

		util.testPost(ENDPOINT_BASE, bd.cAdministrador, status().isUnprocessableEntity()).andDo(requestResult -> {
			ValidationError errors = util.getValidationErrorOf(requestResult);
			assertTrue(errors.getErrors().size() == 2);
			errors.getErrors().stream().allMatch(err -> Arrays.asList("nomeCargo", "descricao").stream()
					.anyMatch(fi -> err.getFieldName().equals(fi)));
		});

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "PUT" })
	public void testUpdate() throws Exception {
		this.bd.getCargoS().save(this.bd.cAdministrador);
		this.util.em().detach(this.bd.cAdministrador);
		this.bd.cAdministrador.setNomeCargo("Auxiliar Administrativo1");

		this.util.testPutExpectNoContent(ENDPOINT_BASE + "/" + this.bd.cAdministrador.getId(), this.bd.cAdministrador);

		Cargo result = this.bd.getCargoS().findById(this.bd.cAdministrador.getId());
		assertTrue(result.getNomeCargo().equals("Auxiliar Administrativo1"));
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "PUT" })
	public void testUpdateNotFound() throws Exception {

		this.util.testPut(ENDPOINT_BASE + "/1", this.bd.cAdministrador, status().isNotFound());
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "PUUT" })
	public void testUpdateSemPermissao() throws Exception {
		this.bd.getCargoS().save(this.bd.cAdministrador);
		this.util.testPutExpectedForbidden(ENDPOINT_BASE + "/" + this.bd.cAdministrador.getId(),
				this.bd.cAdministrador);
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testaFindById() throws Exception {
		this.bd.getCargoS().save(this.bd.cAdministrador);
		util.testGetExpectedSucess(this.ENDPOINT_BASE, bd.cAdministrador.getId());
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
		this.bd.getCargoS().save(this.bd.cAdministrador);
		util.testGet(this.ENDPOINT_BASE, bd.cAdministrador.getId(), status().isForbidden());
	}

	private RestResponsePage<Cargo> getPage(MvcResult result) throws Exception {
		String content = result.getResponse().getContentAsString();
		RestResponsePage<Cargo> responseList = this.util.objectMapper().readValue(content,
				new TypeReference<RestResponsePage<Cargo>>() {
				});
		return responseList;
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void buscaNome() throws Exception {
		this.bd.getCargoS().save(this.bd.cAdministrador);
		this.bd.getCargoS().save(this.bd.cDesenvolvedor);

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("nome", "Desenvolvedor");

		this.util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 1, 1).andDo(resultRequest -> {
			RestResponsePage<Cargo> results = getPage(resultRequest);

			assertTrue(results.get().allMatch(c -> c.getNomeCargo().contains("Desenvolvedor")));
		});

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void buscaNome2() throws Exception {
		this.bd.getCargoS().save(this.bd.cAdministrador);
		this.bd.getCargoS().save(this.bd.cDesenvolvedor);

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

		this.util.testGetPage(ENDPOINT_BASE + "/page", params, status().isOk(), 2, 1).andDo(resultRequest -> {
			RestResponsePage<Cargo> results = getPage(resultRequest);

			assertTrue(results.get().anyMatch(c -> c.getNomeCargo().contains("Desenvolvedor")));
			assertTrue(results.get().anyMatch(c -> c.getNomeCargo().contains("Adm")));
		});

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void buscaNome3() throws Exception {
		this.bd.getCargoS().save(this.bd.cAdministrador);
		this.bd.getCargoS().save(this.bd.cDesenvolvedor);

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("nome", "test");
		this.util.testGetRequestParams(ENDPOINT_BASE + "/page", params, status().isNotFound());

	}

}
