package com.ebm.estoque.domain.interfaces;

import java.math.BigDecimal;

import com.ebm.geral.utils.CalculosItemVenda;

public interface ItemVendaInfo {
	
	Integer getQuantidade();
	BigDecimal getValorVendaLiquido();
	BigDecimal getDesconto();
	BigDecimal getCusto();
	Double getComissaoTx();
	BigDecimal getValorVendaBruto();
	CalculosItemVenda getCalculadora();
}
