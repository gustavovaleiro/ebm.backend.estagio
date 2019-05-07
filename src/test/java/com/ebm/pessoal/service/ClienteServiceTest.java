package com.ebm.pessoal.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ebm.Utils;
import com.ebm.exceptions.DataIntegrityException;
import com.ebm.exceptions.ObjectNotFoundException;
import com.ebm.pessoal.domain.Cidade;
import com.ebm.pessoal.domain.Cliente;
import com.ebm.pessoal.domain.Endereco;
import com.ebm.pessoal.domain.Estado;
import com.ebm.pessoal.domain.PessoaFisica;
import com.ebm.pessoal.domain.PessoaJuridica;
import com.ebm.pessoal.domain.RG;
import com.ebm.pessoal.domain.TipoPessoa;
import com.ebm.pessoal.dtos.ClienteListDTO;


@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ClienteServiceTest {
	
	@Autowired
	private ClienteService clienteService;
	@Autowired
	private PessoaService pessoaService;

	private Estado estadoGO;
	private Cidade goiania;
	private Endereco endereco1;
	private PessoaFisica pf1;
	private PessoaJuridica pj1;
	private Cliente cf1;
	private Cliente cj1;
	
	@Before
	public void setUp() {
		clienteService.deleteAll();
		pessoaService.deleteAll(true);
		
		
		

		estadoGO = new Estado(null, "GO", "Goias");
		goiania = new Cidade(null, "Goiania", estadoGO);
		endereco1 = new Endereco(null, "Test rua tal", "Centro", goiania, "123", "prox ao carai", "12345678", "Endereco residencial");
		pf1 = new PessoaFisica(null, "Joao Da Silva", "02142627668", LocalDate.of(1990, 4, 30), new RG("23123", "SSP", estadoGO ), "Brasileira", goiania);	
		pj1 = new PessoaJuridica(null, "Lanches", "64935609000135", "Lanches ME", "inscricaoEstadual1", "inscricaoMunicipal1");
	
		pf1.getEndereco().add(endereco1);
		pf1.getTelefone().add(Utils.getRandomTelefone());
		pj1.getEndereco().add(endereco1);
		pj1.getTelefone().add(Utils.getRandomTelefone());
		
		
		cf1 = new Cliente(null, pf1, BigDecimal.valueOf(3000), "Cliente tal");
		cj1 = new Cliente(null, pj1, BigDecimal.valueOf(20000), "Empresa tal, cliente desde 1231");
	}
	@After
	public void setDown() {
		clienteService.deleteAll();
		pessoaService.deleteAll(true);
			}
	@Test
	public void testInsercaoSemPessoaDevLancarException() {
		
		try {
			cf1.setPessoa(null);
			clienteService.save(cf1);
			fail("Falha tentando inserir cliente sem pessoa associada, era esperado Data Integration Exception");
			
		}catch(DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(ClienteService.DATAINTEGRITY_CLIENTWITHOUTPERSON));
		}
	}
	
	@Test
	public void testInsercaoComPessoaNaoPersistida() {
		
		clienteService.save(cf1);
		
		assertNotNull(cf1.getId());
		assertNotNull(cf1.getPessoa().getId());
		assertThat(cf1.getId(), equalTo(cf1.getPessoa().getId()));
	}
	
	
	@Test
	public void testInsercaoComPessoaJaPersistida() {
		
		pf1 = (PessoaFisica) pessoaService.save(pf1);
		
		cf1.setPessoa(pf1);
		cf1 = clienteService.save(cf1);
		
		assertNotNull(cf1.getId());
		assertNotNull(cf1.getPessoa().getId());
		assertThat(cf1.getId(), equalTo(cf1.getPessoa().getId()));
	}
	
	@Test
	public void testInsercaoClienteComPessoaQueJaPertenceAOutroCliente() {
		cj1.setPessoa(cf1.getPessoa());
		cf1 = clienteService.save(cf1);
		
		try {
			cj1 = clienteService.save(cj1);
			fail("Falha insercao de cliente com pessoa que ja pertence a outro cliente, deve lançar DataIntegratyExcpetion");
		} catch(DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(ClienteService.DATAINTEGRITY_DUPLICATEPERSON));
		}
		
	}
	
	@Test
	public void testUpdateSemMudarPessoa() {
		cf1 = clienteService.save(cf1);
		cf1.setDescricao("novadescricao");
		cf1.getPessoa().setNome("novonome");
		cf1 = clienteService.save(cf1);
		
		assertThat(cf1.getDescricao(), equalTo("novadescricao"));
		assertThat(cf1.getPessoa().getNome(), equalTo("novonome"));
	}
	@Test
	public void testUpdateMudarPessoa() {
		cf1 = clienteService.save(cf1);
		cf1.setDescricao("novadescricao");
		cf1.setPessoa(pj1);
		
		try {
			cf1 = clienteService.save(cf1);
			fail();
		}catch(DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(ClienteService.DATAINTEGRITY_CHANCEPERSON));
		}
	}
	@Test
	public void testUpdateClienteComPessoaPertenceOutroCliente() {
		cf1 = clienteService.save(cf1);
		cj1 = clienteService.save(cj1);
		
		cf1.setPessoa(cj1.getPessoa());
		try {
			cf1 = clienteService.save(cf1);
			fail("Falha insercao de cliente com pessoa que ja pertence a outro cliente, deve lançar DataIntegratyExcpetion");
		}catch(DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(ClienteService.DATAINTEGRITY_DUPLICATEPERSON));
		}
	}
	
	@Test
	public void testFindCnpj() {
		clienteService.save(cj1);
		
		Cliente result = clienteService.findByCpfOrCnpj(((PessoaJuridica) cj1.getPessoa()).getCnpj());
		
		assertNotNull(result.getId());
		assertThat(((PessoaJuridica) result.getPessoa()).getCnpj(), equalTo(((PessoaJuridica) cj1.getPessoa()).getCnpj()));
	}
	@Test
	public void testFindCPF() {
		clienteService.save(cf1);
		
		Cliente result = clienteService.findByCpfOrCnpj(((PessoaFisica) cf1.getPessoa()).getCpf());
		
		assertNotNull(result.getId());
		assertThat(((PessoaFisica) result.getPessoa()).getCpf(), equalTo(((PessoaFisica) result.getPessoa()).getCpf()));
	}
	
	@Test
	public void testFindCPFEx() {
	
		try {
			 clienteService.findByCpfOrCnpj(((PessoaFisica) cf1.getPessoa()).getCpf());
			fail();
		}catch(ObjectNotFoundException ex) {
			assertThat(ex.getMessage(), equalTo(PessoaService.NOT_FOUND_DOCUMENT + ((PessoaFisica) cf1.getPessoa()).getCpf())  );
		}
	}
	private void cenarioParaBuscaParamiterizada() {
		PessoaFisica pf2 = new PessoaFisica(null, "Joao Snow", "52935546032", LocalDate.of(1995, 3, 30), new RG("3234", "SSP", estadoGO ), "Brasileira", goiania);
		PessoaFisica pf3 = new PessoaFisica(null, "Maria Silva", "07952632019", LocalDate.of(1980, 4, 30), new RG("54345", "SSP", estadoGO ), "Brasileira", goiania);
		PessoaFisica pf4 = new PessoaFisica(null, "Maria Carvalho", "58522943060", LocalDate.of(1990, 1, 30), new RG("4523", "SSP", estadoGO ), "Brasileira", goiania);
		PessoaJuridica pj2 = new PessoaJuridica(null, "Juniscleids ME", "18038642000145", "Juniscleids ME", "inscricaoEstadual2", "inscricaoMunicipal2");
		PessoaJuridica pj3 = new PessoaJuridica(null, "Joao Dev", "46530490000139", "Profissionais ME", "inscricaoEstadual3", "inscricaoEstadual3");
		PessoaJuridica pj4 = new PessoaJuridica(null, "Mercado ME", "84912087000163", "Mercado ME", "inscricaoEstadual4", "inscricaoMunicipal4");
		Arrays.asList(pf2,pf3,pf4,pj2,pj3,pj4).forEach( p -> {
			p.getEndereco().add(endereco1);
			p.getTelefone().add(Utils.getRandomTelefone());
		});
		
		
		Cliente cf2 = new Cliente(null, pf2, BigDecimal.valueOf(1233), "12312");
		Cliente cf3 = new Cliente(null, pf3, BigDecimal.valueOf(1233), "12312");
		Cliente cf4 = new Cliente(null, pf4, BigDecimal.valueOf(2412), "descricao");
		Cliente cj2 = new Cliente(null, pj2, new BigDecimal(2133), "sdaf");
		Cliente cj3 = new Cliente(null, pj3, BigDecimal.valueOf(1233), "12312");
		Cliente cj4 = new Cliente(null, pj4, BigDecimal.valueOf(1233), "12312");

		
		clienteService.saveAll(Arrays.asList(cf1,cf2,cf3,cf4,cj1,cj2,cj3,cj4));
	}
	
	
	@Transactional
	@Test
	public void testaBuscaParamiterizadaPessoaFisica() {

		cenarioParaBuscaParamiterizada();
		
		// executa
		PageRequest pageRequest = PageRequest.of(0, 5);
		Page<ClienteListDTO> result = clienteService.findBy(TipoPessoa.PESSOA_FISICA, null, pageRequest );
		
		//verifica
		assertThat(result.getNumberOfElements(), equalTo(4));
		assertTrue(result.get().allMatch( c-> c.getTipo().equals(TipoPessoa.PESSOA_FISICA.getDescricao())));
		assertFalse(result.get().anyMatch( c-> c.getTipo().equals(TipoPessoa.PESSOA_JURIDICA.getDescricao())));
	}
	
	@Transactional
	@Test
	public void testaBuscaParamiterizadaPessoaJuridica() {

		cenarioParaBuscaParamiterizada();
		
		// executa
		PageRequest pageRequest = PageRequest.of(0, 5);
		Page<ClienteListDTO> result = clienteService.findBy(TipoPessoa.PESSOA_JURIDICA, null, pageRequest );
		
		//verifica
		assertThat(result.getNumberOfElements(), equalTo(4));
		assertTrue(result.get().allMatch( c-> c.getTipo().equals(TipoPessoa.PESSOA_JURIDICA.getDescricao())));
		assertFalse(result.get().anyMatch( c-> c.getTipo().equals(TipoPessoa.PESSOA_FISICA.getDescricao())));
	}
	@Transactional
	@Test
	public void testaBuscaParamiterizadaPFOuPJ() {

		cenarioParaBuscaParamiterizada();
		
		// executa
		PageRequest pageRequest = PageRequest.of(0, 8);
		Page<ClienteListDTO> result = clienteService.findBy(null, null, pageRequest );
		
		//verifica
		assertThat(result.getNumberOfElements(), equalTo(8));
		assertTrue(result.get().anyMatch( c-> c.getTipo().equals(TipoPessoa.PESSOA_FISICA.getDescricao())));
		assertTrue(result.get().anyMatch( c-> c.getTipo().equals(TipoPessoa.PESSOA_JURIDICA.getDescricao())));
	}
	
	@Transactional
	@Test
	public void testaBuscaParamiterizadaNome() {

		cenarioParaBuscaParamiterizada();
		
		// executa
		PageRequest pageRequest = PageRequest.of(0, 8);
		Page<ClienteListDTO> result = clienteService.findBy(null, "joao", pageRequest );
		
		//verifica
		assertThat(result.getNumberOfElements(), equalTo(3));
		assertTrue(result.get().filter( c-> c.getTipo().equals(TipoPessoa.PESSOA_FISICA.getDescricao())).count() == 2 );
		assertTrue(result.get().anyMatch( c-> c.getTipo().equals(TipoPessoa.PESSOA_JURIDICA.getDescricao())));		

	}
	
	@Transactional
	@Test
	public void testaBuscaParamiterizadaNomeAndTipo() {

		cenarioParaBuscaParamiterizada();
		
		// executa
		PageRequest pageRequest = PageRequest.of(0, 8);
		Page<ClienteListDTO> result = clienteService.findBy(TipoPessoa.PESSOA_FISICA, "joao", pageRequest );
		
		//verifica
		assertThat(result.getNumberOfElements(), equalTo(2));
		assertTrue(result.get().allMatch(c-> c.getTipo().equals(TipoPessoa.PESSOA_FISICA.getDescricao())) );

	}
}
