package com.ebm.geral.utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ebm.estoque.domain.ProdutoMovimentacao;
import com.ebm.geral.service.PopulaBD;

@ActiveProfiles("testauto")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CalculosItemVendaTest {

	@Autowired
	private PopulaBD bd;
	private ProdutoMovimentacao pm1;
	private ProdutoMovimentacao pm2;
	private ProdutoMovimentacao pm3;

	@Before
	public void set_up() {
		// Comissao = 0.01, lucro = 0.3, // custo 100
		bd.instanciaItem(true);
		pm1 = new ProdutoMovimentacao(null, bd.p1, BigDecimal.valueOf(0), BigDecimal.valueOf(200), 2);
		pm2 = new ProdutoMovimentacao(null, bd.p1, null, BigDecimal.valueOf(300), 4);
		pm3 = new ProdutoMovimentacao(null, bd.p1, BigDecimal.valueOf(10), BigDecimal.valueOf(300), 4);
	}

	@Test
	public void testComissaoUnitaria() {
		assertThat(pm1.getCalculadora().getComissaoUnitaria().doubleValue(), equalTo(2d));
		assertThat(pm2.getCalculadora().getComissaoUnitaria().doubleValue(), equalTo(3d));
		assertThat(pm3.getCalculadora().getComissaoUnitaria().doubleValue(), equalTo(2.9d));
	}

	@Test
	public void testComissaoTotal() {
		assertThat(pm1.getCalculadora().getComissaoTotal().doubleValue(), equalTo(4d));
		assertThat(pm2.getCalculadora().getComissaoTotal().doubleValue(), equalTo(12d));
		assertThat(pm3.getCalculadora().getComissaoTotal().doubleValue(), equalTo(2.9 * 4d));
	}

	@Test
	public void testLucroBrutoUnitario() {
		assertThat(pm1.getCalculadora().getLucroBrutoUnitario().doubleValue(), equalTo(100d));
	}

	@Test
	public void testLucroBrutoTotal() {
		assertThat(pm1.getCalculadora().getLucroBrutoTotal().doubleValue(), equalTo(100 * 2d));
	}

	@Test
	public void testLucroLiquidoUnitario() {
		assertThat(pm1.getCalculadora().getLucroLiquidoUnitario().doubleValue(), equalTo(98d));
		assertThat(pm3.getCalculadora().getLucroLiquidoUnitario().doubleValue(), equalTo(187.1d));
	}

	@Test
	public void testLucroLiquidoTotal() {
		assertThat(pm1.getCalculadora().getLucroLiquidoTotal().doubleValue(), equalTo(98 * 2d));
		assertThat(pm3.getCalculadora().getLucroLiquidoTotal().doubleValue(), equalTo(187.1 * 4d));
	}

	@Test
	public void testValorVendaLiquido() {
		assertThat(pm1.getCalculadora().getValorVendaLiquido().doubleValue(), equalTo(200d));
		assertThat(pm3.getCalculadora().getValorVendaLiquido().doubleValue(), equalTo(290d));
	}

	@Test
	public void testValorVendaLiquidoTotal() {
		assertThat(pm1.getCalculadora().getValorVendaLiquidoTotal().doubleValue(), equalTo(200*2d));
		assertThat(pm3.getCalculadora().getValorVendaLiquidoTotal().doubleValue(), equalTo(290*4d));
	}
	
	@Test
	public void testValorTotalDesconto() {
		assertThat(pm1.getCalculadora().getValorTotalDesconto().doubleValue(), equalTo(0d));
		assertThat(pm3.getCalculadora().getValorTotalDesconto().doubleValue(), equalTo(10*4d));
	}
	
	@Test
	public void testValorTotalSemDesconto() {
		assertThat(pm1.getCalculadora().getValorTotalBruto().doubleValue(), equalTo(400d));
		assertThat(pm3.getCalculadora().getValorTotalBruto().doubleValue(), equalTo(300*4d));
	}


}
