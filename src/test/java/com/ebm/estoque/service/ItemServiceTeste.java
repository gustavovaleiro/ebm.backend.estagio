package com.ebm.estoque.service;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import com.ebm.estoque.service.interfaces.CategoriaItemService;
import com.ebm.estoque.service.interfaces.ItemService;
import com.ebm.estoque.service.interfaces.UnidadeService;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ItemServiceTeste {

	@Autowired
	private ItemService itemService;
	@Autowired
	private UnidadeService unidadeService;
	@Autowired
	private CategoriaItemService categoriasService;
	
	@Before
	public void setUp() {
		deleteAll();
		
	}
	@After 
	public void setDown() {
		deleteAll();
	}
	private void deleteAll() {
		itemService.deleteAll();
		unidadeService.deleteAll();
		categoriasService.deleteAll();
		
	}

	
}
