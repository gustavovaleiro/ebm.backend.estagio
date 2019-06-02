package com.ebm.geral.utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ebm.estoque.domain.ProdutoMovimentacao;
import com.ebm.estoque.domain.interfaces.ItemVendaInfo;
import com.ebm.geral.service.PopulaBD;

@ActiveProfiles("testauto")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UtilContabelTest {

	@Autowired
	private PopulaBD bd;
	private ProdutoMovimentacao pm1;
	private ProdutoMovimentacao pm2;
	private ProdutoMovimentacao pm3;
	private List<ItemVendaInfo> pms;

	@Before
	public void set_up() {
		// Comissao = 0.01, lucro = 0.3, // custo 100
		bd.instanciaItem(true);
		pm1 = new ProdutoMovimentacao(null, bd.p1, BigDecimal.valueOf(0), BigDecimal.valueOf(200), 2);
		pm2 = new ProdutoMovimentacao(null, bd.p1, null, BigDecimal.valueOf(300), 4);
		pm3 = new ProdutoMovimentacao(null, bd.p1, BigDecimal.valueOf(10), BigDecimal.valueOf(300), 4);
		pms = Arrays.asList( (ItemVendaInfo) pm1, (ItemVendaInfo)pm2, (ItemVendaInfo)pm3);
	}

	@Test
	public void testComissaoTotalComTaxa() {
		assertThat(UtilContabel.calculaComissaoTotal(pms, 0.1), equalTo(276d));

	}
	
	@Test
	public void testComissaoTotal() {
		assertThat(UtilContabel.calculaComissaoTotal(pms), equalTo(27.6));
	}
	
	@Test
	public void testCalcularLucroBruto() {
		assertThat(UtilContabel.calcularLucroBruto(pms), equalTo(1760d));
	}
	@Test
	public void testcalcularLucroLiquido() {
		assertThat(UtilContabel.calcularLucroLiquido(pms), equalTo(1760d-27.6d));
	}
	@Test
	public void testcalcularLucroLiquidoComTaxa() {
		assertThat(UtilContabel.calcularLucroLiquido(pms, 0.1), equalTo(1760d-276d));
	}
	@Test
	public void testcalcularCustoTotal() {
		assertThat(UtilContabel.calcularCustoTotal(pms), equalTo(1000d));
	}
	
	@Test
	public void testCalcularPrecoVendaBRUTOtOTAL() {
		assertThat(UtilContabel.calcularPrecoValorVendaBrutoTotal(pms), equalTo(2800d));
	}
	@Test
	public void testCalcularPrecoVendaLiquidotOTAL() {
		assertThat(UtilContabel.calcularPrecoValorVendaLiquidoTotal(pms), equalTo(2760d));
	}

}
