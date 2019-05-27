package com.ebm.pessoal.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.Utils;
import com.ebm.exceptions.DataIntegrityException;
import com.ebm.pessoal.domain.Cidade;
import com.ebm.pessoal.domain.Email;
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
	@Autowired
	private CidadeService cidadeService;
	@Autowired
	private PessoalPopulaBD bp;

	@Before
	public void set_up() {
		bp.instanciaPessoa();

	}

	// teste de inserção com endereco, telefone e email, deve passar
	@Transactional
	@Test
	public void testInsercaoPessoaFisica() {
		// preparando o cenario

		// executando teste
		Pessoa result = pessoaService.save(bp.pf1);

		// VALIDANDO
		assertNotNull(result.getId());
		assertThat(result.getEmail().iterator().next().getEmail(),
				equalTo(bp.pf1.getEmail().iterator().next().getEmail()));
		assertNotNull(result.getEndereco().iterator().next().getId());
		assertThat(result.getTelefone().size(), equalTo(1));
	}

	// teste inserção de pessoa fisica com cpf invalido
	@Transactional
	@Test
	public void testInsercaoPessoaFisicaCPFInvalido() {
		// cenario
		bp.pf1.setCpf("1233232323");
		// executando
		try {
			pessoaService.save(bp.pf1);
			fail("Falha. Uma exceção deve ser lançada!");
		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(PessoaService.INVALID_CPF));
		}

	}

	@Transactional
	@Test
	public void testInsercaoPessoaSemEmail() {
		bp.pf1.getEmail().clear();

		try {
			pessoaService.save(bp.pf1);
			fail();
		} catch (DataIntegrityException ex) {
			assertTrue(ex.getMessage().equals(PessoaService.NEED_EMAIL));
		}
	}

	@Transactional
	@Test
	public void testInsercaoPessoaTwoEmailMain() {
		bp.pf1.getEmail().add(Utils.getRandomEmail(bp.pf1, true));

		try {
			pessoaService.save(bp.pf1);
			fail();
		} catch (DataIntegrityException ex) {
			assertTrue(ex.getMessage().equals(PessoaService.MOREONEPRINCIPAL + Email.class.getSimpleName()));

		}
	}

	@Transactional
	@Test
	public void testInsercaoPessoaTwoTelefoneMain() {
		bp.pf1.getTelefone().add(Utils.getRandomTelefone(true));

		try {
			pessoaService.save(bp.pf1);
			fail();
		} catch (DataIntegrityException ex) {
			assertTrue(ex.getMessage().equals(PessoaService.MOREONEPRINCIPAL + Telefone.class.getSimpleName()));
		}
	}

	// test inserção de pessoa fisica com cpf ja cadastrado
	@Transactional
	@Test
	public void testInsercaoPessoaFisicaCpfDuplicado() {
		PessoaFisica pf2 = new PessoaFisica(null, "Test", bp.pf1.getCpf(), LocalDate.of(1990, 4, 30),
				new RG("23123", "SSP", bp.estadoGO), "Brasileira", bp.goiania);
		pf2.getEndereco().add(bp.endereco1);
		pf2.getTelefone().add(Utils.getRandomTelefone(true));
		// executando
		pessoaService.save(bp.pf1);

		// validando
		try {
			pessoaService.save(pf2);
			fail("Falha. Uma exceção deve ser lançada!");
		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(PessoaService.DUPLICATE_CPF));
		}

	}

	@Transactional
	@Test
	public void testInsercaoList() {
		pessoaService.saveAll(Arrays.asList(bp.pf1, bp.pj1)).forEach(p -> assertNotNull(p.getId()));
		;
	}

	// Test de inserção de pj;
	@Transactional
	@Test
	public void testaInsercaoPessoaJuridica() {

		PessoaJuridica retorno = (PessoaJuridica) pessoaService.save(bp.pj1);

		// validando
		assertThat(retorno.getCnpj(), equalTo(bp.pj1.getCnpj()));

	}

	// test de inserção de pj com cnpj invalido
	@Transactional
	@Test
	public void testInsercaoPessoaJuridicaCnpjInvalido() {
		// cenario
		bp.pj1.setCnpj("2312412312");
		// executando
		try {
			pessoaService.save(bp.pj1);
			fail("Falha. Uma exceção deve ser lançada!");
		} catch (DataIntegrityException ex) {
			// validando
			assertThat(ex.getMessage(), equalTo(PessoaService.INVALID_CNPJ));
		}

	}

	// teste de pj com cnpj duplicado
	@Transactional
	@Test
	public void testInsercaoPessoaJuridicaCnpjDuplicado() {
		// cenario
		PessoaJuridica pj2 = new PessoaJuridica(null, "TEST", bp.pj1.getCnpj(), "AADFAS", "DFS", "ADSFA");

		// executando

		pessoaService.save(bp.pj1);

		// validando
		try {
			pessoaService.save(pj2);
			fail("Falha. Uma exceção deve ser lançada!");
		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(PessoaService.DUPLICATE_CNPJ));
		}
	}

	// testes de alteraçoes
	@Transactional
	@Test
	public void testUpdateCidade() {
		this.bp.pf1 = (PessoaFisica) pessoaService.save(bp.pf1);

		Cidade naturalidade = new Cidade(null, "Mineiros", new Estado(null, "GO", "gOAS"));
		cidadeService.save(naturalidade);
		bp.pf1.setNaturalidade(naturalidade);
		bp.pf1.setNome("Gustavo");
		bp.pf1.setRG(null);
		bp.pf1 = (PessoaFisica) pessoaService.save(bp.pf1);

		// validando
		assertThat(bp.pf1.getNaturalidade().getEstado().getNome(), equalTo("gOAS"));
		assertThat(bp.pf1.getNome(), equalTo("Gustavo"));

	}

	// testes de alteraçoes
	@Transactional
	@Test
	public void testUpdatePJ() {
		this.bp.pj1 = (PessoaJuridica) pessoaService.save(bp.pj1);

		bp.pj1.setNome("Gustavo ME");
		bp.pj1 = (PessoaJuridica) pessoaService.save(bp.pj1);

		// validando
		assertThat(bp.pj1.getNome(), equalTo("Gustavo ME"));

	}

	// testes de buscas

}
