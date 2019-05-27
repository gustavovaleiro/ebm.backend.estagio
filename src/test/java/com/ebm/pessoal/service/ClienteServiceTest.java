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
	@Autowired
	private PessoalPopulaBD	 bd;
	
	@Before
	public void setUp() {
	
		bd.instanciaCliente(true);
		pessoaService.saveAll(Arrays.asList(bd.pj1,bd.pf1));
	}

	@Transactional
	@Test
	public void testInsercaoSemPessoaDevLancarException() {
		
		try {
			bd.cf1.setPessoa(null);
			clienteService.save(bd.cf1);
			fail("Falha tentando inserir cliente sem pessoa associada, era esperado Data Integration Exception");
			
		}catch(DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(ClienteService.DATAINTEGRITY_CLIENTWITHOUTPERSON));
		}
	}
	@Transactional
	@Test
	public void testInsercaoComPessoa() {
		
		clienteService.save(bd.cf1);
		
		assertNotNull(bd.cf1.getId());
		assertNotNull(bd.cf1.getPessoa().getId());
		assertThat(bd.cf1.getId(), equalTo(bd.cf1.getPessoa().getId()));
	}

	@Test
	public void testInsercaoComPessoaApenasId() {
		bd.cf1.setPessoa(null);
		PessoaFisica pf = new PessoaFisica();
		pf.setId(bd.pf1.getId());
		bd.cf1.setPessoa(pf);
		clienteService.save(bd.cf1);
		
		assertNotNull(bd.cf1.getId());
		assertNotNull(bd.cf1.getPessoa().getId());
		assertNotNull(bd.pf1.getNacionalidade());
		assertNotNull(bd.pf1.getNome());
		assertThat(bd.cf1.getPessoa().getNome(), equalTo(bd.pf1.getNome()));
		assertThat(bd.cf1.getId(), equalTo(bd.cf1.getPessoa().getId()));
		clienteService.delete(bd.cf1.getId());
		pessoaService.deleteAll(true);
	}
	
	@Transactional
	@Test
	public void testInsercaoClienteComPessoaQueJaPertenceAOutroCliente() {
		bd.cj1.setPessoa(bd.cf1.getPessoa());
		bd.cf1 = clienteService.save(bd.cf1);
		
		try {
			bd.cj1 = clienteService.save(bd.cj1);
			fail("Falha insercao de cliente com pessoa que ja pertence a outro cliente, deve lançar DataIntegratyExcpetion");
		} catch(DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(ClienteService.DATAINTEGRITY_DUPLICATEPERSON));
		}
		
	}
	@Transactional
	@Test
	public void testUpdateSemMudarPessoa() {
		bd.cf1 = clienteService.save(bd.cf1);
		bd.cf1.setDescricao("novadescricao");
		bd.cf1.getPessoa().setNome("novonome");
		bd.cf1 = clienteService.save(bd.cf1);
		
		assertThat(bd.cf1.getDescricao(), equalTo("novadescricao"));
		assertThat(bd.cf1.getPessoa().getNome(), equalTo("novonome"));
	}
	@Transactional
	@Test
	public void testUpdateMudarPessoa() {
		bd.cf1 = clienteService.save(bd.cf1);
		bd.cf1.setDescricao("novadescricao");
		bd.cf1.setPessoa(bd.pj1);
		
		try {
			bd.cf1 = clienteService.save(bd.cf1);
			fail();
		}catch(DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(ClienteService.DATAINTEGRITY_CHANCEPERSON));
		}
	}
	@Transactional
	@Test
	public void testUpdateClienteComPessoaPertenceOutroCliente() {
		bd.cf1 = clienteService.save(bd.cf1);
		bd.cj1 = clienteService.save(bd.cj1);
		
		bd.cf1.setPessoa(bd.cj1.getPessoa());
		try {
			bd.cf1 = clienteService.save(bd.cf1);
			fail("Falha insercao de cliente com pessoa que ja pertence a outro cliente, deve lançar DataIntegratyExcpetion");
		}catch(DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(ClienteService.DATAINTEGRITY_DUPLICATEPERSON));
		}
	}
	@Transactional
	@Test
	public void testFindCnpj() {
		clienteService.save(bd.cj1);
		
		Cliente result = clienteService.findByCpfOrCnpj(((PessoaJuridica) bd.cj1.getPessoa()).getCnpj());
		
		assertNotNull(result.getId());
		assertThat(((PessoaJuridica) result.getPessoa()).getCnpj(), equalTo(((PessoaJuridica) bd.cj1.getPessoa()).getCnpj()));
	}
	@Transactional
	@Test
	public void testFindCPF() {
		clienteService.save(bd.cf1);
		
		Cliente result = clienteService.findByCpfOrCnpj(((PessoaFisica) bd.cf1.getPessoa()).getCpf());
		
		assertNotNull(result.getId());
		assertThat(((PessoaFisica) result.getPessoa()).getCpf(), equalTo(((PessoaFisica) result.getPessoa()).getCpf()));
	}
	@Transactional
	@Test
	public void testFindCPFEx() {
	
		try {
			 clienteService.findByCpfOrCnpj("05909561162");
			fail();
		}catch(ObjectNotFoundException ex) {
			assertThat(ex.getMessage(), equalTo(PessoaService.NOT_FOUND_DOCUMENT + "05909561162")  );
		}
	}
	private void cenarioParaBuscaParamiterizada() {
		pessoaService.saveAll(Arrays.asList(bd.pf2,bd.pf3,bd.pf4,bd.pj2,bd.pj3,bd.pj4));
		clienteService.saveAll(Arrays.asList(bd.cf1,bd.cf2,bd.cf3,bd.cf4,bd.cj1,bd.cj2,bd.cj3,bd.cj4));
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
