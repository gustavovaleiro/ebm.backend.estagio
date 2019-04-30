package com.ebm.estoque.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ebm.estoque.domain.Unidade;
import com.ebm.estoque.service.interfaces.UnidadeService;
import com.ebm.exceptions.DataIntegrityException;


@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UnidadeServiceTest {
	
	
	@Autowired
	private UnidadeService unidadeService;
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
	
	
	
	
}
