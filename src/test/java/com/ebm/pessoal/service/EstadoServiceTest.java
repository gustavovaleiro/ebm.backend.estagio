package com.ebm.pessoal.service;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.exceptions.DataIntegrityException;
import com.ebm.exceptions.ObjectNotFoundException;
import com.ebm.pessoal.domain.Cidade;
import com.ebm.pessoal.domain.Estado;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class EstadoServiceTest {
		
	@Autowired
	private EstadoService estadoService;
	private Estado eGO;
	private Estado eMT;
	private boolean deleteAll = true;
	@Autowired
	private CidadeService cidadeService;
	
	@Before
	public  void setUp() {
		estadoService.deleteAll(false);
		eGO = new Estado(null, "GO", "Goias");
		eMT = new Estado(null, "MT", "Mato Grosso");
		
	}
	
	//Test Insercao de Estado com uf invalida
	@Test
	public void testInsercaoUfInvalida() {
		eGO.setUF("ASD");
		
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
		assertThat(result.getUF(), equalTo( eGO.getUF()));
	}
	
	//test update alterando uf e nome
	@Test
	public void testUpdateUFAndNome() {
		eGO = estadoService.save(eGO);
		Estado eNew = new Estado(eGO.getId(), "MT", "Mato Grosso");
		eNew = estadoService.save(eNew);
		
		assertThat(eNew.getId(), equalTo(eGO.getId()));
		assertThat(eNew.getUF(), equalTo("MT"));
		assertThat(eNew.getNome(), equalTo("Mato Grosso"));
	}
	//test update alterando  nome e id passandocomo null
	@Test
	public void testUpdateNome() {
		eGO = estadoService.save(eGO);
		Estado eNew = new Estado(null, eGO.getUF(),"Mato Grosso");
		eNew = estadoService.save(eNew);
		
		assertThat(eNew.getId(), equalTo(eGO.getId()));
		assertThat(eNew.getUF(), equalTo(eGO.getUF()));
		assertThat(eNew.getNome(), equalTo("Mato Grosso"));
		assertThat(estadoService.count(), equalTo(1L));
	}
	//test de busca por uf,  deve encontrar a uf buscada
	@Test
	public void testBuscaUf() {
		estadoService.save(eGO);
		
		Estado result = estadoService.findByUf(eGO.getUF());
		
		assertThat(result.getUF(), equalTo(eGO.getUF()));
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
		assertThat(result.getUF(), equalTo(eGO.getUF()));
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
		
		estadoService.deleteByUf(result.getUF());
		
		try {
			estadoService.findByUf(result.getUF());
			fail();
		} catch (ObjectNotFoundException ex) {
			assertThat(ex.getMessage(), equalTo((EstadoService.NOTFOUND_UF + result.getUF())));
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
			assertThat(ex.getMessage(), equalTo(estadoService.DATAINTEGRITY_ETADOCOMCIDADE));
		}
		cidadeService.delete(cidade);
	}
	
	
}
