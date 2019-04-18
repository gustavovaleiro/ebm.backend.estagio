package com.ebm.pessoal.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
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
import com.ebm.pessoal.domain.Cliente;
import com.ebm.pessoal.domain.Email;
import com.ebm.pessoal.domain.Endereco;
import com.ebm.pessoal.domain.Estado;
import com.ebm.pessoal.domain.PessoaFisica;
import com.ebm.pessoal.domain.PessoaJuridica;
import com.ebm.pessoal.domain.RG;
import com.ebm.pessoal.domain.Telefone;


@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ClienteServiceTest {
	
	@Autowired
	private ClienteService clienteService;
	@Autowired
	private PessoaService pessoaService;
	
	private Telefone telefone1;
	private Telefone telefone2;
	private Email email1;
	private Estado estadoGO;
	private Cidade goiania;
	private Endereco endereco1;
	private PessoaFisica pf1;
	private PessoaJuridica pj1;
	private Cliente c1;
	private Cliente c2;
	
	@Before
	public void setUp() {
		clienteService.deleteAll();
		pessoaService.deleteAll(true);
		
		
		
		telefone1 = new Telefone(null, "66", "43423423", "Celular");
		telefone2 = new Telefone(null, "65", "434223423", "Celular");
		email1 = new Email("tes43t@gmail.com", "");
		estadoGO = new Estado(null, "GO", "Goias");
		goiania = new Cidade(null, "Goiania", estadoGO);
		endereco1 = new Endereco(null, "Test rua tal", "Centro", goiania, "123", "prox ao carai", "12345678", "Endereco residencial");
		pf1 = new PessoaFisica(null, "Joao Da Silva", "56661050004", LocalDate.of(1990, 4, 30), new RG("23123", "SSP", estadoGO ), "Brasileira", goiania);	
		pj1 = new PessoaJuridica(null, "Lanches", "99787331000180", "Lanches ME", "inscricaoEstadual1", "inscricaoMunicipal1");
	
		pf1.getEndereco().add(endereco1);
		pf1.getTelefone().add(telefone1);
		pj1.getEndereco().add(endereco1);
		pj1.getTelefone().add(telefone2);
		
		
		c1 = new Cliente(null, pf1, BigDecimal.valueOf(3000), "Cliente tal");
		c2 = new Cliente(null, pj1, BigDecimal.valueOf(20000), "Empresa tal, cliente desde 1231");
	}
	
	@Test
	public void testInsercaoSemPessoaDevLancarException() {
		
		try {
			c1.setPessoa(null);
			clienteService.save(c1);
			fail("Falha tentando inserir cliente sem pessoa associada, era esperado Data Integration Exception");
			
		}catch(DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(ClienteService.DATAINTEGRITY_CLIENTWITHOUTPERSON));
		}
	}
	
	@Test
	public void testInsercaoComPessoaNaoPersistida() {
		
		clienteService.save(c1);
		
		assertNotNull(c1.getId());
		assertNotNull(c1.getPessoa().getId());
		assertThat(c1.getId(), equalTo(c1.getPessoa().getId()));
	}
	
	
	@Test
	public void testInsercaoComPessoaJaPersistida() {
		
		pf1 = (PessoaFisica) pessoaService.save(pf1);
		
		c1.setPessoa(pf1);
		c1 = clienteService.save(c1);
		
		assertNotNull(c1.getId());
		assertNotNull(c1.getPessoa().getId());
		assertThat(c1.getId(), equalTo(c1.getPessoa().getId()));
	}
	
	@Test
	public void testInsercaoClienteComPessoaQueJaPertenceAOutroCliente() {
		c2.setPessoa(c1.getPessoa());
		c1 = clienteService.save(c1);
		
		try {
			c2 = clienteService.save(c2);
			fail("Falha insercao de cliente com pessoa que ja pertence a outro cliente, deve lançar DataIntegratyExcpetion");
		} catch(DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(ClienteService.DATAINTEGRITY_DUPLICATEPERSON));
		}
		
	}
	
	@Test
	public void testUpdateSemMudarPessoa() {
		c1 = clienteService.save(c1);
		c1.setDescricao("novadescricao");
		c1.getPessoa().setNome("novonome");
		c1 = clienteService.save(c1);
		
		assertThat(c1.getDescricao(), equalTo("novadescricao"));
		assertThat(c1.getPessoa().getNome(), equalTo("novonome"));
	}
	@Test
	public void testUpdateMudarPessoa() {
		c1 = clienteService.save(c1);
		c1.setDescricao("novadescricao");
		c1.setPessoa(pj1);
		
		assertThat(c1.getDescricao(), equalTo("novadescricao"));
		assertThat(c1.getPessoa().getNome(), equalTo(pj1.getNome()));
	}
	@Test
	public void testUpdateClienteComPessoaPertenceOutroCliente() {
		c1 = clienteService.save(c1);
		c2 = clienteService.save(c2);
		
		c1.setPessoa(c2.getPessoa());
		try {
			c1 = clienteService.save(c1);
			fail("Falha insercao de cliente com pessoa que ja pertence a outro cliente, deve lançar DataIntegratyExcpetion");
		}catch(DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(ClienteService.DATAINTEGRITY_DUPLICATEPERSON));
		}
	}
}
