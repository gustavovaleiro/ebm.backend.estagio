package com.ebm.pessoal.service;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.BaseTest;
import com.ebm.geral.exceptions.DataIntegrityException;
import com.ebm.geral.exceptions.ObjectNotFoundException;
import com.ebm.pessoal.domain.Cidade;
import com.ebm.pessoal.domain.Estado;

public class EstadoServiceTest  extends BaseTest{
		
	@Autowired
	private EstadoService estadoService;
	private Estado eGO;
	@Autowired
	private CidadeService cidadeService;
	@Autowired
	private EnderecoService enderecoService;
	
	@Before
	public  void setUp() {
		enderecoService.deleteAll();
		cidadeService.deleteAll(true);
		eGO = new Estado(null, "GO", "Goias");		
	}
	
	//Test Insercao de Estado com uf invalida
	@Test
	public void testInsercaoUfInvalida() {
		eGO.setUf("ASD");
		
		try {
			estadoService.save(eGO);
			fail();
		} catch(DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(EstadoService.UF_INVALIDO));
		}
	}
	//test de inserção
	@Test
	public void AtestInsercao() {
		
		Estado result = estadoService.save(eGO);
		
		assertTrue(estadoService.existe(eGO));
		assertThat(result.getUf(), equalTo( eGO.getUf()));
	}
	
	//test update alterando uf e nome
	@Test
	public void testUpdateUFAndNome() {
		eGO = estadoService.save(eGO);
		Estado eNew = new Estado(eGO.getId(), "MT", "Mato Grosso");
		eNew = estadoService.save(eNew);
		
		assertThat(eNew.getId(), equalTo(eGO.getId()));
		assertThat(eNew.getUf(), equalTo("MT"));
		assertThat(eNew.getNome(), equalTo("Mato Grosso"));
	}
	//test update alterando  nome e id passandocomo null
	@Test
	public void testUpdateNome() {
		eGO = estadoService.save(eGO);
		Estado eNew = new Estado(null, eGO.getUf(),"Mato Grosso");
		eNew = estadoService.save(eNew);
		
		assertThat(eNew.getId(), equalTo(eGO.getId()));
		assertThat(eNew.getUf(), equalTo(eGO.getUf()));
		assertThat(eNew.getNome(), equalTo("Mato Grosso"));
		assertThat(estadoService.count(), equalTo(1L));
	}
	//test de busca por uf,  deve encontrar a uf buscada
	@Test
	public void testBuscaUf() {
		estadoService.save(eGO);
		
		Estado result = estadoService.findByUf(eGO.getUf());
		
		assertThat(result.getUf(), equalTo(eGO.getUf()));
		assertThat(result.getNome(), equalTo(eGO.getNome()));	
	}
	
	
	//test de busca por uf, nao deve encontrar
	@Test
	public void testBuscaUfN() {
		
		try {
			estadoService.findByUf("GO");
			fail();
		}catch( ObjectNotFoundException ex) {
			assertThat(ex.getMessage(), equalTo(EstadoService.NOTFOUND_UF + "GO"));
		}
	}
	
	//test de busca por id,  deve encontrar a o estado buscado
	@Test
	public void testBuscaEstadoId() {
		estadoService.save(eGO);
		
		Estado result = estadoService.find(eGO.getId());
		
		assertThat(result.getId(), equalTo(eGO.getId()));
		assertThat(result.getUf(), equalTo(eGO.getUf()));
		assertThat(result.getNome(), equalTo(eGO.getNome()));	
	}
	
	
	//test de busca por id, nao deve encontrar estourando um erro
	@Test
	public void testBuscaIdN() {
		
		try {
			estadoService.find(1);
			fail();
		}catch( ObjectNotFoundException ex) {
			assertThat(ex.getMessage(), equalTo(EstadoService.NOTFOUND_ID + 1));
		}
	}
	
	// teste de delete por id
	@Test
	public void testDeleteId() {
		Estado result = estadoService.save(eGO);
		
		estadoService.delete(result.getId());
		
		try {
			estadoService.find(result.getId());
			fail();
		} catch (ObjectNotFoundException ex) {
			assertThat(ex.getMessage(), equalTo((EstadoService.NOTFOUND_ID + result.getId())));
		}
		
	}
	@Test
	public void testDeleteUF() {
		Estado result = estadoService.save(eGO);
		
		estadoService.deleteByUf(result.getUf());
		
		try {
			estadoService.findByUf(result.getUf());
			fail();
		} catch (ObjectNotFoundException ex) {
			assertThat(ex.getMessage(), equalTo((EstadoService.NOTFOUND_UF + result.getUf())));
		}
		
	}
	
	//tenta deletar um estado que esta associado a uma cidade sem suprimir a exção deve falhar
	@Test
	@Transactional
	public void testExcecaoDIEnoDeleteDeEstadoComCidade() {
		Cidade cidade = new Cidade(null, "Cidade de teste", eGO);
	    estadoService.save(eGO);
		cidade = cidadeService.save(cidade);
		
		try {
			estadoService.delete(eGO.getId());
			fail("Era esperado lançar uma exception ao tentar deletar o estado que tivesse uma cidade");
		} catch(DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(EstadoService.DATAINTEGRITY_ETADOCOMCIDADE));
		}
		cidadeService.delete(cidade);
	}
}
