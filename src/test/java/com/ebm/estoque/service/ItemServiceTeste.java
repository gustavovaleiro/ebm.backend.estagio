package com.ebm.estoque.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ebm.estoque.domain.CategoriaItem;
import com.ebm.estoque.domain.Item;
import com.ebm.estoque.domain.Produto;
import com.ebm.estoque.domain.Servico;
import com.ebm.estoque.domain.Unidade;
import com.ebm.estoque.service.interfaces.ItemService;
import com.ebm.exceptions.DataIntegrityException;

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

	private Collection<Item> allItens;
	
	@Before
	public void setUp() {
		deleteAll();
		
		un1 = new Unidade(null, "un", "Unidade");
		un2 = new Unidade(null, "hrs", "Horas");
		cat1 = new CategoriaItem(null, "Computadores");
		cat2 = new CategoriaItem(null, "Manutenção em computadores");
		
		p1 = new Produto(null, "Computador", "Computador i5 8gbRam", un1, cat1, "COM01",BigDecimal.valueOf(100), null, 0.3, 0.01, 5, 0, 10);
		s1 = new Servico(null, "Limpeza Cooler", "Limpesa cooler", un2, cat2, "LIMP02", BigDecimal.valueOf(100), BigDecimal.valueOf(20), 0.5, 0.02);
		
	
	}
	@After 
	public void setDown() {
		deleteAll();
	}
	private void deleteAll() {
		itemService.deleteAll(true);		
	}

	@Test
	public void  testaInsercaoNormal() {
		p1 = (Produto) itemService.save(p1);
		
		assertNotNull(p1.getId());
		assertThat(p1.getEstoqueMinimo(), equalTo(5));
	}
	
	@Test
	public void  testaInsercaoServiceNormal() {
		s1 = (Servico) itemService.save(s1);
		
		assertNotNull(s1.getId());

	}
	
	@Test
	public void  testaUpdate() {
		p1 = (Produto) itemService.save(p1);
		p1.setNome("NOVONOME");
		p1 = (Produto) itemService.save(p1);
		assertNotNull(p1.getId());
		assertThat(p1.getNome(), equalTo("NOVONOME"));
	}
	
	@Test
	public void  testaInsercaoSemUnidade() {
		p1.setUnidade(null);
		try {
			p1 = (Produto) itemService.save(p1);
			fail();
		}catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(ItemService.DATAINTEGRITY_UNIDADENULL));
		}
	}
	
	@Test
	public void  testaInsercaoSemCategoria() {
		p1.setCategoria(null);
		try {
			p1 = (Produto) itemService.save(p1);
			fail();
		}catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(ItemService.DATAINTEGRITY_CATEGORIANULL));
		}
	}
	
	@Test
	public void testComissaoEstimadaComTaxaFixa() {
		Double comissaoResultado = itemService.calcularComissaoEstimada(Arrays.asList(p1,s1), 0.1);
		assertTrue(comissaoResultado - 31 ==0);
	}
	
	@Test
	public void testComissaoEstimadaComTaxaItem() {
		Double comissaoResultado = itemService.calcularComissaoEstimada(Arrays.asList(p1,s1));
		assertTrue(comissaoResultado - 4.9 ==0);
	}
	
	
	@Test
	public void testCalculoLucroBruto() {
		Double resultado = itemService.calcularLucroBrutoEstimado(Arrays.asList(p1,s1));
		assertTrue(resultado - 90 ==0);
	}
	
	@Test
	public void testCalculoLucroLiquido() {
		Double resultado = itemService.calcularLucroLiquidoEstimado(Arrays.asList(p1,s1));
		assertTrue(resultado - 85.1 ==0);
	}
	
	@Test
	public void testCalculoLucroLiquidoComTaxaComissaoFixa() {
		Double resultado = itemService.calcularLucroLiquidoEstimado(Arrays.asList(p1,s1),0.1);
		assertTrue(resultado -59 ==0);
	}
	
	@Test
	public void testCalculoCustoTotal() {
		Double resultado = itemService.calcularCustoTotal(Arrays.asList(p1,s1));
		assertTrue(resultado -220 ==0);
	}
	
	@Test
	public void testCalculoPrecoVendaTotal() {
		Double resultado = itemService.calcularPrecoVendaTotal(Arrays.asList(p1,s1));
		assertTrue(resultado -310 ==0);
	}
	
	
	
	private void preparaTestParameterizado() {
		CategoriaItem cat3 = new CategoriaItem(null, "Perifericos");
		Produto p2 = Produto.of("Teclado Mecanico RGB", un1, cat3);
		Produto p3 = Produto.of("Teclado membrana", un1, cat3);
		Produto p4 = Produto.of("Computador i3  4gbram", un1, cat3);
		

		Servico s2 = Servico.of("Troca de fonte", un1, cat2);
		Servico s3 = Servico.of("Montagem Computador", un2, cat1);
		Servico s4 = Servico.of("Formatacao", un1, cat2);
		
		allItens = itemService.saveAll(Arrays.asList(p1,p2,p3,p4,s1,s2,s3,s4));
	}
	//test find parameterizado com tipo apenas
	//test find parameterizado unidade
	//tes find parameterizado com nome
	// test find parameterizado com desc
	//test find parameterizado com categoria
	//test find parameterizado cod interno
	// test find parameterizado com unidade e categoria
	// test find parameterizado com unidade nome e tipo
	// test find parameterizado com tudo
	
}
