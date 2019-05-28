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
import com.ebm.exceptions.DataIntegrityException;
import com.ebm.exceptions.ObjectNotFoundException;

@ActiveProfiles("test")
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
		} catch (DataIntegrityException ex) {
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
		} catch (DataIntegrityException ex) {
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

	@Transactional
	@Test
	public void testDeleteNotFound() {
		try {
			categoriaService.deleteById(2);
			fail();
		} catch (ObjectNotFoundException ex) {
			assertThat(ex.getMessage(), equalTo(CategoriaItemService.ONFE_NOTFOUNDBYID + 2));
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
	public void testDelete() {

		cat1 = categoriaService.save(cat1);
		int id = cat1.getId();
		categoriaService.deleteById(id);
		try {
			categoriaService.findById(id);
			fail();
		} catch (ObjectNotFoundException ex) {
			assertThat(ex.getMessage(), equalTo(CategoriaItemService.ONFE_NOTFOUNDBYID + id));
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
