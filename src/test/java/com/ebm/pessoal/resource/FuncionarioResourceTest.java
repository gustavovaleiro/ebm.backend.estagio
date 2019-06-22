package com.ebm.pessoal.resource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;

import com.ebm.BaseTest;
import com.ebm.geral.exceptions.DataIntegrityException;
import com.ebm.geral.exceptions.ObjectNotFoundException;
import com.ebm.geral.resource.exception.ValidationError;
import com.ebm.geral.service.PopulaBD;
import com.ebm.geral.utils.Utils;
import com.ebm.pessoal.domain.Cargo;
import com.ebm.pessoal.domain.Funcionario;
import com.ebm.pessoal.domain.PessoaFisica;
import com.ebm.pessoal.domain.PessoaJuridica;
import com.ebm.pessoal.domain.TipoPessoa;
import com.ebm.pessoal.dtos.FuncionarioListDTO;

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
//	@Transactional
//	@Test
//	public void testFindCnpj() {
//		funcionarioService.save(bd.funj1);
//
//		Funcionario result = funcionarioService.findByCpfOrCnpj(((PessoaJuridica) bd.funj1.getPessoa()).getCnpj());
//
//		assertNotNull(result.getId());
//		assertThat(((PessoaJuridica) result.getPessoa()).getCnpj(),
//				equalTo(((PessoaJuridica) bd.funj1.getPessoa()).getCnpj()));
//	}
//
//	@Transactional
//	@Test
//	public void testFindCPF() {
//		funcionarioService.save(bd.funf1);
//
//		Funcionario result = funcionarioService.findByCpfOrCnpj(((PessoaFisica) bd.funf1.getPessoa()).getCpf());
//
//		assertNotNull(result.getId());
//		assertThat(((PessoaFisica) result.getPessoa()).getCpf(), equalTo(((PessoaFisica) result.getPessoa()).getCpf()));
//	}
//
//	@Transactional
//	@Test
//	public void testFindCPFEx() {
//
//		try {
//			funcionarioService.findByCpfOrCnpj("05909561162");
//			fail();
//		} catch (ObjectNotFoundException ex) {
//			assertThat(ex.getMessage(), equalTo(PessoaService.NOT_FOUND_DOCUMENT + "05909561162"));
//		}
//	}
//
//	private void cenarioParaBuscaParamiterizada() {
//		pessoaService.saveAll(Arrays.asList(bd.pf2, bd.pf3, bd.pf4, bd.pj2, bd.pj3, bd.pj4));
//		funcionarioService
//				.saveAll(Arrays.asList(bd.funf1, bd.funf2, bd.funf3, bd.funf4, bd.funj1, bd.funj2, bd.funj3, bd.funj4));
//
//	}
//
//	@Transactional
//	@Test
//	public void testaBuscaParamiterizadaPessoaFisica() {
//
//		cenarioParaBuscaParamiterizada();
//
//		// executa
//		PageRequest pageRequest = PageRequest.of(0, 8);
//		Page<FuncionarioListDTO> result = funcionarioService.findBy(TipoPessoa.PESSOA_FISICA, null, null, null,
//				pageRequest);
//
//		// verifica
//		assertThat(result.getNumberOfElements(), equalTo(4));
//		assertTrue(result.get().allMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_FISICA.getDescricao())));
//		assertFalse(result.get().anyMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_JURIDICA.getDescricao())));
//	}
//
//	@Transactional
//	@Test
//	public void testaBuscaParamiterizadaPessoaJuridica() {
//
//		cenarioParaBuscaParamiterizada();
//
//		// executa
//		PageRequest pageRequest = PageRequest.of(0, 5);
//		Page<FuncionarioListDTO> result = funcionarioService.findBy(TipoPessoa.PESSOA_JURIDICA, null, null, null,
//				pageRequest);
//
//		// verifica
//		assertThat(result.getNumberOfElements(), equalTo(4));
//		assertTrue(result.get().allMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_JURIDICA.getDescricao())));
//		assertFalse(result.get().anyMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_FISICA.getDescricao())));
//	}
//
//	@Transactional
//	@Test
//	public void testaBuscaParamiterizadaPFOuPJ() {
//
//		cenarioParaBuscaParamiterizada();
//
//		// executa
//		PageRequest pageRequest = PageRequest.of(0, 8);
//		Page<FuncionarioListDTO> result = funcionarioService.findBy(null, null, null, null, pageRequest);
//
//		// verifica
//		assertThat(result.getNumberOfElements(), equalTo(8));
//		assertTrue(result.get().anyMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_FISICA.getDescricao())));
//		assertTrue(result.get().anyMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_JURIDICA.getDescricao())));
//	}
//
//	@Transactional
//	@Test
//	public void testaBuscaParamiterizadaNome() {
//
//		cenarioParaBuscaParamiterizada();
//
//		// executa
//		PageRequest pageRequest = PageRequest.of(0, 8);
//		Page<FuncionarioListDTO> result = funcionarioService.findBy(null, null, "joao", null, pageRequest);
//
//		// verifica
//		assertThat(result.getNumberOfElements(), equalTo(3));
//		assertTrue(result.get().filter(c -> c.getTipo().equals(TipoPessoa.PESSOA_FISICA.getDescricao())).count() == 2);
//		assertTrue(result.get().anyMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_JURIDICA.getDescricao())));
//
//	}
//
//	@Transactional
//	@Test
//	public void testaBuscaParamiterizadaNomeAndTipo() {
//
//		cenarioParaBuscaParamiterizada();
//
//		// executa
//		PageRequest pageRequest = PageRequest.of(0, 8);
//		Page<FuncionarioListDTO> result = funcionarioService.findBy(TipoPessoa.PESSOA_FISICA, null, "joao", null,
//				pageRequest);
//
//		// verifica
//		assertThat(result.getNumberOfElements(), equalTo(2));
//		assertTrue(result.get().allMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_FISICA.getDescricao())));
//
//	}
//
//	@Transactional
//	@Test
//	public void testaBuscaParamiterizadaAllNull() {
//
//		cenarioParaBuscaParamiterizada();
//
//		// executa
//		PageRequest pageRequest = PageRequest.of(0, 8);
//		Page<FuncionarioListDTO> result = funcionarioService.findBy(null, null, null, null, pageRequest);
//
//		// verifica
//		assertThat(result.getNumberOfElements(), equalTo(8));
//		assertTrue(result.get().anyMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_FISICA.getDescricao())));
//		assertTrue(result.get().anyMatch(c -> c.getTipo().equals(TipoPessoa.PESSOA_JURIDICA.getDescricao())));
//	}
//
//	@Transactional
//	@Test
//	public void testaBuscaParamiterizadaCargo() {
//
//		cenarioParaBuscaParamiterizada();
//
//		// executa
//		PageRequest pageRequest = PageRequest.of(0, 8);
//		Page<FuncionarioListDTO> result = funcionarioService.findBy(null, bd.cAdministrador.getNomeCargo(), null, null,
//				pageRequest);
//
//		// verifica
//		assertThat(result.getNumberOfElements(), equalTo(4));
//		assertTrue(result.get().allMatch(f -> f.getCargo().equals(bd.cAdministrador.getNomeCargo())));
//		assertFalse(result.get().anyMatch(f -> f.getCargo().equals(bd.cDesenvolvedor.getNomeCargo())));
//	}
//
//	@Transactional
//	@Test
//	public void testaBuscaParamiterizadaCargoAndTipoNothing() {
//
//		cenarioParaBuscaParamiterizada();
//
//		// executa
//		PageRequest pageRequest = PageRequest.of(0, 8);
//		Page<FuncionarioListDTO> result = funcionarioService.findBy(TipoPessoa.PESSOA_FISICA,
//				bd.cAdministrador.getNomeCargo(), null, null, pageRequest);
//
//		// verifica
//		assertThat(result.getNumberOfElements(), equalTo(0));
//	}
//
//	@Transactional
//	@Test
//	public void testaBuscaParamiterizadaCargoAndTipo() {
//
//		cenarioParaBuscaParamiterizada();
//		PessoaJuridica pj = new PessoaJuridica(null, "Dev Me", "93811961000167", "DevME", null, null);
//		pj.getEndereco().add(bd.endereco1);
//		pj.getTelefone().add(Utils.getRandomTelefone(true));
//		pj.getEmail().add(Utils.getRandomEmail(pj, true));
//		Funcionario f = new Funcionario(null, pj, "Dev-06", bd.cDesenvolvedor, LocalDate.now(), 0.,
//				bd.cDesenvolvedor.getSalarioBase());
//		pessoaService.save(pj);
//		funcionarioService.save(f);
//		// executa
//		PageRequest pageRequest = PageRequest.of(0, 8);
//		Page<FuncionarioListDTO> result = funcionarioService.findBy(TipoPessoa.PESSOA_JURIDICA,
//				bd.cDesenvolvedor.getNomeCargo(), null, null, pageRequest);
//
//		// verifica
//		assertThat(result.getNumberOfElements(), equalTo(1));
//		assertTrue(result.get().allMatch(fd -> fd.getCargo().equals(bd.cDesenvolvedor.getNomeCargo())));
//		assertTrue(result.get().allMatch(fd -> fd.getTipo().equals(TipoPessoa.PESSOA_JURIDICA.getDescricao())));
//	}
//
//	@Transactional
//	@Test
//	public void testaBuscaParamiterizadaCargoAndNome() {
//
//		cenarioParaBuscaParamiterizada();
//
//		// executa
//		PageRequest pageRequest = PageRequest.of(0, 8);
//		Page<FuncionarioListDTO> result = funcionarioService.findBy(null, bd.cDesenvolvedor.getNomeCargo(), "MaRiA",
//				null, pageRequest);
//
//		// verifica
//		assertThat(result.getNumberOfElements(), equalTo(2));
//		assertTrue(result.get().allMatch(f -> f.getCargo().equals(bd.cDesenvolvedor.getNomeCargo())));
//		assertFalse(result.get().anyMatch(f -> f.getNome().equals("Maria")));
//	}
//
//	@Transactional
//	@Test
//	public void testaBuscaParamiterizadaMatricula() {
//
//		cenarioParaBuscaParamiterizada();
//
//		// executa
//		PageRequest pageRequest = PageRequest.of(0, 8);
//		Page<FuncionarioListDTO> result = funcionarioService.findBy(null, null, null, "adm", pageRequest);
//		// verifica
//		assertThat(result.getNumberOfElements(), equalTo(4));
//		assertTrue(result.get().allMatch(f -> f.getMatricula().toLowerCase().contains("adm")));
//	}
	
	
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
