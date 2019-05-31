package com.ebm.estoque.domain.interfaces;

import java.math.BigDecimal;

public interface CalculavelContabel {

	BigDecimal getPrecoVenda();

	BigDecimal getComissaoEstimada();

	BigDecimal getLucroEstimado();

	BigDecimal getCustoTotal();

}
