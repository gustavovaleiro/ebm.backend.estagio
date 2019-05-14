package com.ebm.pessoal.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ebm.Utils;
import com.ebm.exceptions.DataIntegrityException;
import com.ebm.pessoal.domain.Cidade;
import com.ebm.pessoal.domain.Email;
import com.ebm.pessoal.domain.Endereco;
import com.ebm.pessoal.domain.Estado;
import com.ebm.pessoal.domain.Pessoa;
import com.ebm.pessoal.domain.PessoaFisica;
import com.ebm.pessoal.domain.PessoaJuridica;
import com.ebm.pessoal.domain.RG;
import com.ebm.pessoal.domain.Telefone;

import dev.gustavovalerio.DevApplicationTests;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PessoaServiceTest extends DevApplicationTests {

	@Autowired
	private PessoaService pessoaService;

	private Telefone telefone1;

	private Endereco endereco1;
	private Estado estadoGO;
	private Cidade goiania;
	private PessoaFisica pf1;
	private PessoaJuridica pj1;

	private Telefone telefone2;

	@Before
	public void set_up() {
		pessoaService.deleteAll(true);

		telefone1 = new Telefone(null, "66", "43423423", "Celular", true);
		telefone2 = new Telefone(null, "65", "434223423", "Celular", true);
		estadoGO = new Estado(null, "GO", "Goias");
		goiania = new Cidade(null, "Goiania", estadoGO);
		endereco1 = new Endereco(null, "Test rua tal", "Centro", goiania, "123", "prox ao carai", "12345678",
				"Endereco residencial", true);
		pf1 = new PessoaFisica(null, "Joao Da Silva", "56661050004", LocalDate.of(1990, 4, 30),
				new RG("23123", "SSP", estadoGO), "Brasileira", goiania);
		pj1 = new PessoaJuridica(null, "Lanches", "99787331000180", "Lanches ME", "inscricaoEstadual1",
				"inscricaoMunicipal1");

		pf1.getEndereco().add(endereco1);
		pf1.getTelefone().add(telefone1);

		pj1.getEndereco().add(endereco1);
		pj1.getTelefone().add(telefone2);
		pj1.getEmail().add(Utils.getRandomEmail(pj1, true));
		pf1.getEmail().add(Utils.getRandomEmail(pf1, true));
	}
	@After
	public void setDown() {
		pessoaService.deleteAll(true);
	}
	// teste de inserção com endereco, telefone e email, deve passar
	@Test
	public void testInsercaoPessoaFisica() {
		// preparando o cenario

		// executando teste
		Pessoa result = pessoaService.save(pf1);

		// VALIDANDO
		assertNotNull(result.getId());
		assertThat(result.getEmail().iterator().next().getEmail(), equalTo(pf1.getEmail().iterator().next().getEmail()));
		assertNotNull(result.getEndereco().iterator().next().getId());
		assertThat(result.getTelefone().size(), equalTo(1));
	}

	// teste inserção de pessoa fisica com cpf invalido
	@Test
	public void testInsercaoPessoaFisicaCPFInvalido() {
		// cenario
		pf1.setCpf("1233232323");
		// executando
		try {
			pessoaService.save(pf1);
			fail("Falha. Uma exceção deve ser lançada!");
		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(PessoaService.INVALID_CPF));
		}

	}
	
	@Test
	public void testInsercaoPessoaSemEmail() {
		pf1.getEmail().clear();
		
		try {
			pessoaService.save(pf1);
			fail();
		}catch(DataIntegrityException ex) {
			assertTrue(ex.getMessage().equals(PessoaService.NEED_EMAIL));
		}
	}
	
	@Test
	public void testInsercaoPessoaTwoEmailMain() {
		pf1.getEmail().add(Utils.getRandomEmail(pf1, true));
		System.out.println(pf1.getEmail().size());
	    pf1.getEmail().stream().filter(e -> e.isPrincipal()).forEach(e -> System.out.println(e.getEmail()));
		try {
			pessoaService.save(pf1);
			fail();
		}catch(DataIntegrityException ex) {
			assertTrue(ex.getMessage().equals(PessoaService.MOREONEPRINCIPAL + Email.class.getSimpleName()));
	
		}
	}
	
	@Test
	public void testInsercaoPessoaTwoTelefoneMain() {
		pf1.getTelefone().add(Utils.getRandomTelefone(true));
		
		try {
			pessoaService.save(pf1);
			fail();
		}catch(DataIntegrityException ex) {
			assertTrue(ex.getMessage().equals(PessoaService.MOREONEPRINCIPAL + Telefone.class.getSimpleName()));
		}
	}

	// test inserção de pessoa fisica com cpf ja cadastrado
	@Test
	public void testInsercaoPessoaFisicaCpfDuplicado() {
		PessoaFisica pf2 = new PessoaFisica(null, "Test", pf1.getCpf(), LocalDate.of(1990, 4, 30),
				new RG("23123", "SSP", estadoGO), "Brasileira", goiania);
		pf2.getEndereco().add(endereco1);
		pf2.getTelefone().add(telefone2);
		// executando
		pessoaService.save(pf1);

		// validando
		try {
			pessoaService.save(pf2);
			fail("Falha. Uma exceção deve ser lançada!");
		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(PessoaService.DUPLICATE_CPF));
		}

	}

	@Test
	public void testInsercaoList() {
		pessoaService.saveAll(Arrays.asList(pf1, pj1)).forEach(p -> assertNotNull(p.getId()));
		;
	}

	// Test de inserção de pj;
	@Test
	public void testaInsercaoPessoaJuridica() {

		PessoaJuridica retorno = (PessoaJuridica) pessoaService.save(pj1);

		// validando
		assertThat(retorno.getCnpj(), equalTo(pj1.getCnpj()));

	}

	// test de inserção de pj com cnpj invalido
	@Test
	public void testInsercaoPessoaJuridicaCnpjInvalido() {
		// cenario
		pj1.setCnpj("2312412312");
		// executando
		try {
			pessoaService.save(pj1);
			fail("Falha. Uma exceção deve ser lançada!");
		} catch (DataIntegrityException ex) {
			// validando
			assertThat(ex.getMessage(), equalTo(PessoaService.INVALID_CNPJ));
		}

	}

	// teste de pj com cnpj duplicado
	@Test
	public void testInsercaoPessoaJuridicaCnpjDuplicado() {
		// cenario
		PessoaJuridica pj2 = new PessoaJuridica(null, "TEST", pj1.getCnpj(), "AADFAS", "DFS", "ADSFA");

		// executando

		pessoaService.save(pj1);

		// validando
		try {
			pessoaService.save(pj2);
			fail("Falha. Uma exceção deve ser lançada!");
		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(PessoaService.DUPLICATE_CNPJ));
		}
	}

	// testes de alteraçoes
	@Test
	public void testUpdateCidade() {
		this.pf1 = (PessoaFisica) pessoaService.save(pf1);

		pf1.setNaturalidade(new Cidade(null, "Mineiros", new Estado(null, "GO", "gOAS")));
		pf1.setNome("Gustavo");
		pf1.setRG(null);
		pf1 = (PessoaFisica) pessoaService.save(pf1);

		// validando
		assertThat(pf1.getNaturalidade().getEstado().getNome(), equalTo("gOAS"));
		assertThat(pf1.getNome(), equalTo("Gustavo"));

	}

	// testes de alteraçoes
	@Test
	public void testUpdateCidadePJ() {
		this.pj1 = (PessoaJuridica) pessoaService.save(pj1);

		pj1.setNome("Gustavo ME");
		pj1 = (PessoaJuridica) pessoaService.save(pj1);

		// validando
		assertThat(pj1.getNome(), equalTo("Gustavo ME"));

	}

	// testes de buscas

}
