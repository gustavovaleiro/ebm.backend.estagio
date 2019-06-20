package com.ebm.estoque.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.BaseTest;
import com.ebm.estoque.domain.CategoriaItem;
import com.ebm.estoque.domain.Produto;
import com.ebm.estoque.domain.Unidade;
import com.ebm.estoque.service.interfaces.CategoriaItemService;
import com.ebm.estoque.service.interfaces.ItemService;
import com.ebm.estoque.service.interfaces.UnidadeService;
import com.ebm.geral.exceptions.DataIntegrityException;
import com.ebm.geral.exceptions.ObjectNotFoundException;


public class UnidadeServiceTest extends BaseTest{
	
	
	@Autowired
	private UnidadeService unidadeService;
	@Autowired
	private CategoriaItemService categorias;
	@Autowired
	private ItemService itens;
	private Unidade un1;
	
	@Before
	public void setUp() {
	
		unidadeService.deleteAll();
		
		 un1 = new Unidade(null, "UN", "Unidade");
	}
	@After
	public void setDown() {
		unidadeService.deleteAll();
	}
	
	@Test
	public void testaAdicionarUnidadeDeveAceitar() {
		un1 = unidadeService.save(un1);
		
		assertNotNull(un1.getId());
	}
	
	@Test
	public void testaAdicionarUnidadeSemAbreviacaoEx() {
		un1.setAbrev(null);
		try {
			un1 = unidadeService.save(un1);
			fail();
		}catch(DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(UnidadeService.DATAINTEGRITY_ABREVNULL));
		}
		
	}
	@Test
	public void testaAdicionarUnidadeSemNomeEx() {
		un1.setNome(null);
		try {
			un1 = unidadeService.save(un1);
			fail();
		}catch(DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(UnidadeService.DATAINTEGRITY_NOMENULL));
		}
		
	}
	@Test
	public void testaAdicionarUnidadeAbreviacaoRepitida() {
		Unidade un2 = new Unidade(null, un1.getAbrev(), "asdf");
		unidadeService.save(un1);
		try {
			un2 = unidadeService.save(un2);
			fail();
		}catch(DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(UnidadeService.DATAINTEGRITY_DUPLICATEABRE));
		}
		
	}
	
	@Test
	public void testeUpdate() {
		un1 = unidadeService.save(un1);
		un1.setNome("outronome");
		Unidade un2 = unidadeService.save(un1);
		
		assertNotNull(un1.getId());
		assertThat(un2.getId(), equalTo(un1.getId()));
		assertThat(un2.getNome(), equalTo("outronome"));
	}
	
	@Transactional
	@Test
	public void testDeleteNotFound() {
		try {
			unidadeService.deleteById(2);
			fail();
		}catch(ObjectNotFoundException ex) {
			assertThat(ex.getMessage(), equalTo(UnidadeService.ONFE_NOTFOUNDBYID+2));
		}
	}
	
	@Transactional
	@Test
	public void testDeleteExIdNull() {
		try {
			unidadeService.deleteById(null);
			fail();
		}catch(DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(UnidadeService.DATAINTEGRITY_IDNULL));
		}
	}
	
	@Transactional
	@Test
	public void testDelete() {
	
		un1 = unidadeService.save(un1);
		int id = un1.getId();
		unidadeService.deleteById(id);
		try {
			unidadeService.findById(id);
			fail();
		}catch(ObjectNotFoundException ex) {
			assertThat(ex.getMessage(), equalTo(UnidadeService.ONFE_NOTFOUNDBYID+id));
		}
	}
	
	
	@Transactional
	@Test
	public void testDeleteComItem() {
		CategoriaItem cat1 = new CategoriaItem(null, "test");
		cat1 = categorias.save(cat1);
		un1 = unidadeService.save(un1);
		Produto p = Produto.of("tal", un1,cat1);
		p.setEstoque(5, 2, 4);
		p.setMargemLucro(0.1);
		p.setComissaoVenda(0.2);
		p.setValorCompraMedio(BigDecimal.valueOf(2));
		itens.save(p);
		
		
		int id = un1.getId();
		
		try {
			unidadeService.deleteById(id);
			fail();
		}catch(DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(UnidadeService.DATAINTEGRITY_UNITHASITEM));
		}
	}
	
}
