package com.ebm.estoque.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

import com.ebm.estoque.domain.CategoriaItem;
import com.ebm.estoque.domain.Item;
import com.ebm.estoque.domain.Produto;
import com.ebm.estoque.domain.Servico;
import com.ebm.estoque.domain.Unidade;
import com.ebm.estoque.domain.enums.TipoItem;
import com.ebm.estoque.dtos.ItemListDTO;
import com.ebm.estoque.service.interfaces.ItemService;
import com.ebm.exceptions.DataIntegrityException;
import com.ebm.exceptions.ObjectNotFoundException;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ItemServiceTeste {

	@Autowired
	private ItemService itemService;

	private Unidade un1;
	private Unidade un2;
	private CategoriaItem cat1;
	private CategoriaItem cat2;
	private Produto p1;
	private Servico s1;

	private Produto p2;

	private Produto p3;

	private Produto p4;

	private Servico s2;

	private Servico s3;

	private Servico s4;

	private CategoriaItem cat3;

	@Before
	public void setUp() {
		deleteAll();

		un1 = new Unidade(null, "un", "Unidade");
		un2 = new Unidade(null, "hrs", "Horas");

		cat1 = new CategoriaItem(null, "Computadores");

		cat2 = new CategoriaItem(null, "Manutenção em computadores");

		p1 = new Produto(null, "Computador", "Computador i5 8gbRam", un1, cat1, "COM01", BigDecimal.valueOf(100), null,
				0.3, 0.01, 5, 0, 10);
		s1 = new Servico(null, "Limpeza Cooler", "Limpesa cooler", un2, cat2, "LIMP02", BigDecimal.valueOf(100),
				BigDecimal.valueOf(20), 0.5, 0.02);

	}

	@After
	public void setDown() {
		deleteAll();
	}

	private void deleteAll() {
		itemService.deleteAll(true);
	}

	@Test
	public void testaInsercaoNormal() {
		p1 = (Produto) itemService.save(p1);

		assertNotNull(p1.getId());
		assertThat(p1.getEstoqueMinimo(), equalTo(5));
	}

	@Test
	public void testaInsercaoServiceNormal() {
		s1 = (Servico) itemService.save(s1);

		assertNotNull(s1.getId());

	}

	@Test
	public void testaUpdate() {
		p1 = (Produto) itemService.save(p1);
		p1.setNome("NOVONOME");
		p1 = (Produto) itemService.save(p1);
		assertNotNull(p1.getId());
		assertThat(p1.getNome(), equalTo("NOVONOME"));
	}

	@Test
	public void testaInsercaoSemUnidade() {
		p1.setUnidade(null);
		try {
			p1 = (Produto) itemService.save(p1);
			fail();
		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(ItemService.DATAINTEGRITY_UNIDADENULL));
		}
	}

	@Test
	public void testaInsercaoSemCategoria() {
		p1.setCategoria(null);
		try {
			p1 = (Produto) itemService.save(p1);
			fail();
		} catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(ItemService.DATAINTEGRITY_CATEGORIANULL));
		}
	}

	@Test
	public void testComissaoEstimadaComTaxaFixa() {
		Double comissaoResultado = itemService.calcularComissaoEstimada(Arrays.asList(p1, s1), 0.1);
		assertTrue(comissaoResultado - 31 == 0);
	}

	@Test
	public void testComissaoEstimadaComTaxaItem() {
		Double comissaoResultado = itemService.calcularComissaoEstimada(Arrays.asList(p1, s1));
		assertTrue(comissaoResultado - 4.9 == 0);
	}

	@Test
	public void testCalculoLucroBruto() {
		Double resultado = itemService.calcularLucroBrutoEstimado(Arrays.asList(p1, s1));
		assertTrue(resultado - 90 == 0);
	}

	@Test
	public void testCalculoLucroLiquido() {
		Double resultado = itemService.calcularLucroLiquidoEstimado(Arrays.asList(p1, s1));
		assertTrue(resultado - 85.1 == 0);
	}

	@Test
	public void testCalculoLucroLiquidoComTaxaComissaoFixa() {
		Double resultado = itemService.calcularLucroLiquidoEstimado(Arrays.asList(p1, s1), 0.1);
		assertTrue(resultado - 59 == 0);
	}

	@Test
	public void testCalculoCustoTotal() {
		Double resultado = itemService.calcularCustoTotal(Arrays.asList(p1, s1));
		assertTrue(resultado - 220 == 0);
	}

	@Test
	public void testCalculoPrecoVendaTotal() {
		Double resultado = itemService.calcularPrecoVendaTotal(Arrays.asList(p1, s1));
		assertTrue(resultado - 310 == 0);
	}

	private void preparaTestParameterizado() {
		cat3 = new CategoriaItem(null, "Perifericos");
		p2 = Produto.of("Teclado Mecanico RGB", un1, cat3);
		p3 = Produto.of("Teclado membrana", un1, cat3);
		p4 = Produto.of("Computador i3  4gbram", un1, cat3);

		s2 = Servico.of("Troca de fonte", un1, cat2);
		s3 = Servico.of("Montagem Computador", un2, cat1);
		s4 = Servico.of("Formatacao", un1, cat2);

		itemService.saveAll(Arrays.asList(p1, p2, p3, p4, s1, s2, s3, s4));
	}
	@Test
	public void testaFindById() {
		itemService.save(p1);
		Produto result = (Produto) itemService.findById(p1.getId());

		assertThat(result.getId(), equalTo(p1.getId()));
		assertThat(result.getNome(), equalTo(p1.getNome()));

	}
	@Test
	public void testaFindByIdEx() {
		try{
			Produto result = (Produto) itemService.findById(4);
			fail();
		}catch(ObjectNotFoundException ex) {
			assertThat(ex.getMessage(), equalTo(ItemService.ONFE_BYID));
		}
	}
	@Test
	public void testaFindByIdExNUll() {
		try{
			Produto result = (Produto) itemService.findById(null);
			fail();
		}catch(DataIntegrityException ex) {
			
			assertThat(ex.getMessage(), equalTo(ItemService.DATAINTEGRITY_IDNULL));
		}
	}

	// test find parameterizado com tipo
	@Test
	public void testFindParamiterizadoTipoProduto() {
		preparaTestParameterizado();

		Page<ItemListDTO> results = itemService.findBy(null, TipoItem.PRODUTO.getDescricao(), null, null, null,
				PageRequest.of(0, 8));

		assertThat(results.getNumberOfElements(), equalTo(4));
		assertTrue(results.stream().allMatch(i -> i.getTipo() == TipoItem.PRODUTO.getDescricao()));
		assertFalse(results.stream().anyMatch(i -> i.getTipo() == TipoItem.SERVICO.getDescricao()));

	}

	@Test
	public void testFindParamiterizadoTipoServico() {
		preparaTestParameterizado();

		Page<ItemListDTO> results = itemService.findBy(null, TipoItem.SERVICO.getDescricao(), null, null, null,
				PageRequest.of(0, 8));

		assertThat(results.getNumberOfElements(), equalTo(4));
		assertTrue(results.stream().allMatch(i -> i.getTipo() == TipoItem.SERVICO.getDescricao()));
		assertFalse(results.stream().anyMatch(i -> i.getTipo() == TipoItem.PRODUTO.getDescricao()));
	}

	@Test
	public void testFindParamiterizadoTipoNull() {
		preparaTestParameterizado();

		Page<ItemListDTO> results = itemService.findBy(null, null, null, null, null, PageRequest.of(0, 8));

		assertThat(results.getNumberOfElements(), equalTo(8));
		assertTrue(results.stream().anyMatch(i -> i.getTipo() == TipoItem.SERVICO.getDescricao()));
		assertTrue(results.stream().anyMatch(i -> i.getTipo() == TipoItem.PRODUTO.getDescricao()));
	}

	// test find parameterizado unidade s1 s3
	@Test
	public void testFindParameterizadoComUnidade() {
		preparaTestParameterizado();

		Page<ItemListDTO> results = itemService.findBy(null, null, null, un2.getAbrev(), null, PageRequest.of(0, 8));

		assertThat(results.getNumberOfElements(), equalTo(2));
		assertTrue(results.stream().anyMatch(i -> i.getNome().equals(s1.getNome())));
		assertTrue(results.stream().anyMatch(i -> i.getNome().equals(s3.getNome())));

	}

	// tes find parameterizado com nome
	@Test
	public void testFindParameterizadoComNome() {
		preparaTestParameterizado();

		Page<ItemListDTO> results = itemService.findBy(null, null, "TeCLAdo", null, null, PageRequest.of(0, 8));

		assertThat(results.getNumberOfElements(), equalTo(2));
		assertTrue(results.stream().anyMatch(i -> i.getNome().equals(p2.getNome())));
		assertTrue(results.stream().anyMatch(i -> i.getNome().equals(p3.getNome())));

	}

	// test find parameterizado com categoria
	@Test
	public void testFindParameterizadoComCategoria() {
		preparaTestParameterizado();

		Page<ItemListDTO> results = itemService.findBy(null, null, null, null, cat3.getNome(), PageRequest.of(0, 8));
		

		assertThat(results.getNumberOfElements(), equalTo(3));
		assertTrue(results.get().allMatch(i -> i.getCategoria().equals(cat3.getNome())));

	}
	// test find parameterizado cod interno
	@Test
	public void testFindParameterizadoCodInterno() {
		preparaTestParameterizado();

		Page<ItemListDTO> results = itemService.findBy("computador", null, null, null, null, PageRequest.of(0, 8));

		assertThat(results.getNumberOfElements(), equalTo(2));
		assertTrue(results.get().allMatch(i -> i.getCodInterno().toLowerCase().contains("computador")));

	}
	// test find parameterizado com unidade e categoria
	
	@Test
	public void testFindParameterizadoUnidadeECategoria() {
		preparaTestParameterizado();

		Page<ItemListDTO> results = itemService.findBy(null, null, null, un1.getAbrev(), cat2.getNome(), PageRequest.of(0, 8));

		assertThat(results.getNumberOfElements(), equalTo(2));
		assertTrue(results.get().allMatch(i -> i.getUnidade().equals(un1.getAbrev())));
		assertTrue(results.get().allMatch(i -> i.getCategoria().equals(cat2.getNome())));

	}
	// test find parameterizado com tudo	s4 = Servico.of("Formatacao", un1, cat2);
	@Test
	public void testFindParameterizadoTodosOsParametros() {
		preparaTestParameterizado();

		Page<ItemListDTO> results = itemService.findBy(null, TipoItem.SERVICO.getDescricao(), "FoRmAtAcao", un1.getAbrev(), cat2.getNome(), PageRequest.of(0, 8));

		assertThat(results.getNumberOfElements(), equalTo(1));
		assertTrue(results.get().allMatch(i -> i.getUnidade().equals(un1.getAbrev())));
		assertTrue(results.get().allMatch(i -> i.getCategoria().equals(cat2.getNome())));

	}
}
