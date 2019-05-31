package com.ebm.estoque.domain.interfaces;

import java.math.BigDecimal;

public interface ItemVendaInfo {
	
	Integer getQuantidade();
	BigDecimal getValorVendaLiquido();
	BigDecimal getDesconto();
	BigDecimal getCusto();
}
