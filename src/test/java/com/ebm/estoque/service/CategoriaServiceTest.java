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

import com.ebm.estoque.domain.CategoriaItem;
import com.ebm.estoque.service.interfaces.CategoriaItemService;
import com.ebm.exceptions.DataIntegrityException;


@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CategoriaServiceTest {
	
	
	@Autowired
	private CategoriaItemService categoriaService;
	private CategoriaItem cat1;
	
	@Before
	public void setUp() {
	
		categoriaService.deleteAll();
		
		 cat1 = new CategoriaItem(null, "Informatica");
	}
	@After
	public void setDown() {
		categoriaService.deleteAll();
	}
	
	@Test
	public void testaAdicionarCategoriaDeveAceitar() {
		cat1 = categoriaService.save(cat1);
		
		assertNotNull(cat1.getId());
	}

	@Test
	public void testaAdicionarCategoriaSemNomeEx() {
		cat1.setNome(null);
		try {
			cat1 = categoriaService.save(cat1);
			fail();
		}catch(DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(CategoriaItemService.DATAINTEGRITY_NOMENULL));
		}
		
	}
	@Test
	public void testaAdicionarCategoriaNomeRepetido() {
		CategoriaItem un2 = new CategoriaItem(null, cat1.getNome());
		categoriaService.save(cat1);
		try {
			un2 = categoriaService.save(un2);
			fail();
		}catch(DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(CategoriaItemService.DATAINTEGRITY_DUPLICATENOME));
		}
		
	}
	
	@Test
	public void testeUpdate() {
		cat1 = categoriaService.save(cat1);
		cat1.setNome("outronome");
		CategoriaItem un2 = categoriaService.save(cat1);
		
		assertNotNull(cat1.getId());
		assertThat(un2.getId(), equalTo(cat1.getId()));
		assertThat(un2.getNome(), equalTo("outronome"));
	}
	
	
	
	
}
