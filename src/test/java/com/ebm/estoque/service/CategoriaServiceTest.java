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
import org.springframework.transaction.annotation.Transactional;

import com.ebm.estoque.domain.CategoriaItem;
import com.ebm.estoque.domain.Produto;
import com.ebm.estoque.domain.Unidade;
import com.ebm.estoque.service.interfaces.CategoriaItemService;
import com.ebm.estoque.service.interfaces.ItemService;
import com.ebm.estoque.service.interfaces.UnidadeService;
import com.ebm.geral.exceptions.DataIntegrityException;
import com.ebm.geral.exceptions.ObjectNotFoundException;

@ActiveProfiles("testauto")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CategoriaServiceTest {

	@Autowired
	private CategoriaItemService categoriaService;
	@Autowired
	private ItemService itens;
	@Autowired
	private UnidadeService unidades;
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
	public void testaAdicionarCategoriaNomeRepetido() {
		CategoriaItem un2 = new CategoriaItem(null, cat1.getNome());
		categoriaService.save(cat1);
		try {
			un2 = categoriaService.save(un2);
			fail();
		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(CategoriaItemService.DATAINTEGRITY_DUPLICATENOME));
		}

	}


	@Transactional
	@Test
	public void testDeleteExIdNull() {
		try {
			categoriaService.deleteById(null);
			fail();
		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(CategoriaItemService.DATAINTEGRITY_IDNULL));
		}
	}

	@Transactional
	@Test
	public void testDeleteComItem() {
		Unidade u = new Unidade(null, "a", "d");
		cat1 = categoriaService.save(cat1);
		u = unidades.save(u);
		Produto p = Produto.of("tal", u, cat1);
		itens.save(p);

		int id = cat1.getId();

		try {
			categoriaService.deleteById(id);
			fail();
		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(CategoriaItemService.DATAINTEGRITY_CATTHASITEM));
		}
	}

}
