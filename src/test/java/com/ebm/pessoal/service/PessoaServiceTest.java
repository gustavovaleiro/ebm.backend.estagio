package com.ebm.pessoal.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
import com.ebm.pessoal.repository.EnderecoRepository;
import com.ebm.pessoal.repository.PessoaFisicaRepository;
import com.ebm.pessoal.repository.PessoaJuridicaRepository;

import dev.gustavovalerio.DevApplicationTests;



@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PessoaServiceTest extends DevApplicationTests{
	
	
	@Autowired
	private PessoaService pessoaService;

	private Telefone telefone1;
	private Telefone telefone2;
	private Email email1;
	private Email email2;
	private Endereco endereco1;
	private Endereco endereco2;
	private Estado estadoGO;
	private Estado estadoMT;
	private Cidade cuiaba;
	private Cidade goiania;
	private PessoaFisica pf1;
	private PessoaFisica pf2;
	private PessoaJuridica pj1;
	private PessoaJuridica pj2;
	
	
	
	@Before
	public void set_up() {
		pessoaService.deleteAll(true);
		telefone1 = new Telefone(null, "66", "43423423", "Celular");
		telefone2 = new Telefone(null, "33", "54352342", "Residencial");
		email2 = new Email("test@gmail.com", "");
		email1 = new Email("tes43t@gmail.com", "");
		estadoGO = new Estado(null, "GO", "Goias");
		estadoMT = new Estado(null, "MT", "Mato Grosso");
		cuiaba = new Cidade(null, "Cuiaba", estadoMT);
		goiania = new Cidade(null, "Goiania", estadoGO);
		endereco1 = new Endereco(null, "Test rua tal", "Centro", cuiaba, "123", "prox ao carai", "12345678", "Endereco residencial");
		endereco2 = new Endereco(null, "Outrao rua", "bairro qualquer", cuiaba, "43", "prox tal", "12345678", "Endereco profissional");
		pf1 = new PessoaFisica(null, "Joao Da Silva", "56661050004", LocalDate.of(1990, 4, 30), new RG("23123", "SSP", estadoGO ), "Brasileira", goiania);
		pf2 = new PessoaFisica(null, "Joao Snow", "52935546032", LocalDate.of(1995, 3, 30), new RG("3234", "SSP", estadoGO ), "Brasileira", goiania);
		
		pj1 = new PessoaJuridica(null, "Lanches", "99787331000180", "Lanches ME", "inscricaoEstadual1", "inscricaoMunicipal1");
		pj2 = new PessoaJuridica(null, "Juniscleids ME", "18038642000145", "Juniscleids ME", "inscricaoEstadual2", "inscricaoMunicipal2");
	
	}
	
	
	// teste de inserção com endereco, telefone e email, deve passar
	@Test
	public void testInsercaoPessoaFisica() {
		//preparando o cenario
		pf1.getEndereco().add(endereco1);
		pf1.getEmail().add(email1);
		pf1.getTelefone().add(telefone1);
		

		
		//executando teste
		Pessoa result = pessoaService.insert(pf1);
		
		//VALIDANDO
		assertThat(1, equalTo(result.getId()));
		assertThat(result.getEmail().get(0).getEmail(), equalTo(email1.getEmail()));
		assertThat(result.getEndereco().get(0).getId(), equalTo(1));
		assertThat(result.getTelefone().get(0).getNumero(), equalTo(telefone1.getNumero()));
	}
	
	//teste inserção de pessoa fisica com cpf invalido
	@Test
	public void testInsercaoPessoaFisicaCPFInvalido() {
		//cenario
		pf1.setCpf("1233232323");
		
		//executando
		try {
			pessoaService.insert(pf1);
			fail("Falha. Uma exceção deve ser lançada!");
		}catch(DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo("Não foi possivel validar esse cpf, tente novamente com um cpf valido"));
		}
	
	}
	
	//test inserção de pessoa fisica com cpf ja cadastrado
//	public void testInsercaoPessoaFisicaCpfDuplicado
	
	
	//Test de inserção de pj;
	
	//test de inserção de pj com cnpj invalido
	
	//teste de pj com cnpj duplicado
	
	
	

}
