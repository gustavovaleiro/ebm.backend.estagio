package com.ebm.todosteste;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.BaseTest;
import com.ebm.geral.resource.exception.ValidationError;
import com.ebm.geral.service.PopulaBD;
import com.ebm.geral.utils.Utils;
import com.ebm.pessoal.domain.Cidade;
import com.ebm.pessoal.domain.Estado;
import com.ebm.pessoal.domain.PessoaFisica;
import com.ebm.pessoal.domain.PessoaJuridica;
import com.ebm.pessoal.domain.RG;

public class PessoaResourceTest extends BaseTest {

	@Autowired
	private PopulaBD bd;

	private final String ENDPOINT_BASE = "/pessoas";
	private final String BASE_AUTHORITY = "FORNECEDOR_";

	@Before
	public void set_up() {
		bd.instanciaPessoa().associaPessoa();
	}

	// teste de inserção com endereco, telefone e email, deve passar
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testInsercaoPessoaFisica() throws Exception {

		util.testPostExpectCreated(ENDPOINT_BASE, bd.pf1);
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POdST" })
	public void testInsercaoPessoaFisicaSemPermissao() throws Exception {

		util.testPost(ENDPOINT_BASE, bd.pf1, status().isForbidden());
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testInsercaoPessoaJuridica() throws Exception {
		util.testPostExpectCreated(ENDPOINT_BASE, bd.pj1);

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testValidacaoPessoaNotNullEEmpty() throws Exception {
		bd.pf1.setEmail(null);
		bd.pf1.setTelefone(null);
		bd.pf1.setEndereco(null);
		bd.pf1.setNome(null);
		bd.pf1.setTipo(null);

		util.testPost(ENDPOINT_BASE, bd.pf1, status().isUnprocessableEntity()).andDo(requestResult -> {
			ValidationError errors = util.getValidationErrorOf(requestResult);
			assertTrue(errors.getErrors().size() == 9);
			errors.getErrors().stream().allMatch(err -> Arrays.asList("endereco", "email", "telefone", "nome", "tipo")
					.stream().anyMatch(fi -> err.getFieldName().equals(fi)));
		});

	}

	private String getS(int i) {
		return Utils.getRandomString(i);
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testValidacaoPessoaFisicaNotNullEEmpty() throws Exception {
		bd.pf1.setCpf(null);
		bd.pf1.setDataNascimento(null);
		bd.pf1.setRG(null);
		bd.pf1.setNaturalidade(null);
		util.testPost(ENDPOINT_BASE, bd.pf1, status().isUnprocessableEntity()).andDo(requestResult -> {
			ValidationError errors = util.getValidationErrorOf(requestResult);
			assertTrue(errors.getErrors().size() == 5);
			errors.getErrors().stream().allMatch(err -> Arrays.asList("cpf", "dataNascimento", "naturalidade", "rG")
					.stream().anyMatch(fi -> err.getFieldName().equals(fi)));
		});

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testValidacaoPessoaJuridicaNotNullEEmpty() throws Exception {
		bd.pj1.setCnpj(null);
		bd.pj1.setRazaoSocial(null);

		util.testPost(ENDPOINT_BASE, bd.pj1, status().isUnprocessableEntity()).andDo(requestResult -> {
			ValidationError errors = util.getValidationErrorOf(requestResult);
			assertTrue(errors.getErrors().size() == 4);
			errors.getErrors().stream().allMatch(
					err -> Arrays.asList("cnpj", "razaoSocial").stream().anyMatch(fi -> err.getFieldName().equals(fi)));
		});

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testValidacaoPessoaFStringLengthMin() throws Exception {
		bd.pf1.setNome("a");
		bd.pf1.getNaturalidade().setNome(getS(2));
		bd.pf1.getEndereco().get(0).setRua(getS(4));
		bd.pf1.getEndereco().get(0).setBairro(getS(1));
		bd.pf1.getEndereco().get(0).setCEP(getS(7));
		bd.pf1.getEndereco().get(0).getCidade().getEstado().setUF(getS(2));
		bd.pf1.getEmail().get(0).setEmail(getS(5));
		bd.pf1.getTelefone().get(0).setDDD(getS(1));
		bd.pf1.getTelefone().get(0).setNumero(getS(7));
		bd.pf1.setCpf(getS(7));
		bd.pf1.getRG().setRG(getS(4));
		bd.pf1.getRG().setEmissor(getS(3));

		util.testPost(ENDPOINT_BASE, bd.pf1, status().isUnprocessableEntity()).andDo(print()).andDo(requestResult -> {
			ValidationError errors = util.getValidationErrorOf(requestResult);
			
			assertTrue(errors.getErrors().size() == 11);
			errors.getErrors().stream()
					.allMatch(
							err -> Arrays
									.asList("nome", "naturalidade", "rua", "bairro", "CEP", "dDD", "numero", "email",
											"uF", "cpf", "RG", "emissor")
									.stream().anyMatch(fi -> err.getFieldName().equals(fi)));
		});

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testValidacaoPessoaJStringLengthMin() throws Exception {

		bd.pj1.setCnpj(getS(7));
		bd.pj1.setRazaoSocial(getS(2));
		util.testPost(ENDPOINT_BASE, bd.pj1, status().isUnprocessableEntity()).andDo(requestResult -> {
			ValidationError errors = util.getValidationErrorOf(requestResult);
			assertTrue(errors.getErrors().size() == 2);
			errors.getErrors().stream().allMatch(
					err -> Arrays.asList("cnpj", "razaoSocial").stream().anyMatch(fi -> err.getFieldName().equals(fi)));
		});

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testValidacaoPessoaFStringLengthMax() throws Exception {
		bd.pf1.setNome(getS(61));
		bd.pf1.getNaturalidade().setNome(getS(61));
		bd.pf1.getEndereco().get(0).setRua(getS(61));
		bd.pf1.getEndereco().get(0).setBairro(getS(61));
		bd.pf1.getEndereco().get(0).setCEP(getS(12));
		bd.pf1.getEndereco().get(0).getCidade().getEstado().setUF(getS(4));
		bd.pf1.getEmail().get(0).setEmail(getS(61));
		bd.pf1.getTelefone().get(0).setDDD(getS(4));
		bd.pf1.getTelefone().get(0).setNumero(getS(10));
		bd.pf1.getRG().setRG(getS(16));
		bd.pf1.setCpf(getS(12));
		bd.pf1.getRG().setEmissor(getS(13));

		util.testPost(ENDPOINT_BASE, bd.pf1, status().isUnprocessableEntity());
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testValidacaoPessoaJStringLengthMax() throws Exception {

		bd.pj1.setCnpj(getS(17));
		bd.pj1.setRazaoSocial(getS(81));
		util.testPost(ENDPOINT_BASE, bd.pj1, status().isUnprocessableEntity()).andDo(requestResult -> {
			ValidationError errors = util.getValidationErrorOf(requestResult);
			assertTrue(errors.getErrors().size() == 2);
			errors.getErrors().stream().allMatch(
					err -> Arrays.asList("cnpj", "razaoSocial").stream().anyMatch(fi -> err.getFieldName().equals(fi)));
		});

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testInsercaoPessoaTwoEmailMain() throws Exception {
		this.bd.pf1.getEmail().add(Utils.getRandomEmail(this.bd.pf1, true));

		util.testPost(ENDPOINT_BASE, bd.pf1, status().isBadRequest());
	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testInsercaoPessoaTwoTelefoneMain() throws Exception {
		this.bd.pf1.getTelefone().add(Utils.getRandomTelefone(true));

		util.testPost(ENDPOINT_BASE, bd.pf1, status().isBadRequest());
	}

	// test inserção de pessoa fisica com cpf ja cadastrado
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testInsercaoPessoaFisicaCpfDuplicado() throws Exception {
		this.bd.getPessoaS().save(bd.pf1);
		this.bd.pf2.setCpf(this.bd.pf1.getCpf());
		assertTrue(this.bd.pf1.getCpf().equals(this.bd.pf2.getCpf()));
		// executando
		util.testPost(ENDPOINT_BASE, this.bd.pf2, status().isBadRequest());

	}

	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testInsercaoPessoaJuridicaCnpjInvalido() throws Exception {
		// cenario
		this.bd.pj1.setCnpj("2312412312");
		util.testPost(ENDPOINT_BASE, this.bd.pj1, status().isBadRequest());

	}

	// teste de pj com cnpj duplicado
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "POST" })
	public void testInsercaoPessoaJuridicaCnpjDuplicado() throws Exception {
		// cenario
		this.bd.pj2.setCnpj(this.bd.pj1.getCnpj());

		// executando
		this.bd.getPessoaS().save(this.bd.pj1);

		// validando
		util.testPost(ENDPOINT_BASE, this.bd.pj2, status().isBadRequest());
	}

	// testes de alteraçoes
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "PUT" })
	public void testUpdateCidade() throws Exception {
		this.bd.getPessoaS().save(this.bd.pf1);
		this.util.em().detach(this.bd.pf1);
		this.bd.pf1.setNaturalidade(new Cidade(null, "Mineiros", new Estado(null, "GO", "gOAS")));
		this.bd.pf1.setNome("Gustavo");
		
		this.util.testPutExpectNoContent(this.ENDPOINT_BASE +"/" +this.bd.pf1.getId(), this.bd.pf1);
			
		this.bd.pf1 = (PessoaFisica) this.bd.getPessoaS().findById(this.bd.pf1.getId());
		// validando
		assertTrue(this.bd.pf1.getNaturalidade().getEstado().getNome().equals("Goiás"));
		assertTrue(this.bd.pf1.getNome().equals("Gustavo"));
	}

	// testes de alteraçoes
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "PUT" })
	public void testUpdatePJ() throws Exception {
	    this.bd.getPessoaS().save(this.bd.pj1);
		this.util.em().detach(this.bd.pj1);
		this.bd.pj1.setNome("Gustavo ME");
		this.util.testPutExpectNoContent(this.ENDPOINT_BASE +"/" +this.bd.pj1.getId(), this.bd.pj1);
		this.bd.pj1 =  (PessoaJuridica) this.bd.getPessoaS().findById(this.bd.pj1.getId());
		// validando
		assertTrue(this.bd.pj1.getNome().equals("Gustavo ME"));

	}
//
//	// testes de buscas
	
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "GET" })
	public void testaFindById() throws Exception {
		this.bd.getPessoaS().save(this.bd.pf1);
		util.testGetExpectedSucess(this.ENDPOINT_BASE, bd.pf1.getId());
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
		this.bd.getPessoaS().save(this.bd.pf1);
		util.testGet(this.ENDPOINT_BASE, bd.pf1.getId(), status().isForbidden());
	}
	
	@Transactional
	@Test
	@WithMockUser(username = "test", password = "test", authorities = { BASE_AUTHORITY + "DELETE" })
	public void testDeleteSucesso() throws Exception {
		this.bd.getPessoaS().save(this.bd.pf1);
		util.testDelete(ENDPOINT_BASE + "/" + bd.pf1.getId(), status().isNoContent());
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
