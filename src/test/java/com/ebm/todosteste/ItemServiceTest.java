package com.ebm.todosteste;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.BaseTest;
import com.ebm.estoque.service.interfaces.CategoriaItemService;
import com.ebm.estoque.service.interfaces.ItemService;
import com.ebm.estoque.service.interfaces.UnidadeService;
import com.ebm.geral.service.PopulaBD;

public class ItemServiceTest  extends BaseTest{

	@Autowired
	private ItemService itemService;
	@Autowired
	private CategoriaItemService catServ;
	@Autowired
	private UnidadeService uniServ;
	@Autowired
	private PopulaBD bd;

	@Before
	public void setUp() {

		bd.instanciaItem(true);
		catServ.saveAll(Arrays.asList(bd.cat1, bd.cat2));
		uniServ.save(bd.un1);
		uniServ.save(bd.un2);
	}

	

	@Transactional
	@Test
	public void testComissaoEstimadaComTaxaFixa() {
		Double comissaoResultado = itemService.calcularComissaoEstimada(Arrays.asList(bd.p1, bd.s1), 0.1);
		assertTrue(comissaoResultado - 31 == 0);
	}

	@Transactional
	@Test
	public void testComissaoEstimadaComTaxaItem() {
		Double comissaoResultado = itemService.calcularComissaoEstimada(Arrays.asList(bd.p1, bd.s1));
		assertTrue(comissaoResultado - 4.9 == 0);
	}

	@Transactional
	@Test
	public void testCalculoLucroBruto() {
		Double resultado = itemService.calcularLucroBrutoEstimado(Arrays.asList(bd.p1, bd.s1));
		assertTrue(resultado - 90 == 0);
	}

	@Transactional
	@Test
	public void testCalculoLucroLiquido() {
		Double resultado = itemService.calcularLucroLiquidoEstimado(Arrays.asList(bd.p1, bd.s1));
		assertTrue(resultado - 85.1 == 0);
	}

	@Transactional
	@Test
	public void testCalculoLucroLiquidoComTaxaComissaoFixa() {
		Double resultado = itemService.calcularLucroLiquidoEstimado(Arrays.asList(bd.p1, bd.s1), 0.1);
		assertTrue(resultado - 59 == 0);
	}

	@Transactional
	@Test
	public void testCalculoCustoTotal() {
		Double resultado = itemService.calcularCustoTotal(Arrays.asList(bd.p1, bd.s1));
		assertTrue(resultado - 220 == 0);
	}

	@Transactional
	@Test
	public void testCalculoPrecoVendaTotal() {
		Double resultado = itemService.calcularPrecoVendaTotal(Arrays.asList(bd.p1, bd.s1));
		assertTrue(resultado - 310 == 0);
	}

}