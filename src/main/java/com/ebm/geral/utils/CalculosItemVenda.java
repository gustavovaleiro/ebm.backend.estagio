package com.ebm.geral.utils;

import java.math.BigDecimal;
import java.util.Optional;

import com.ebm.estoque.domain.interfaces.ItemVendaInfo;

public class CalculosItemVenda {

	private ItemVendaInfo item;

	public CalculosItemVenda(ItemVendaInfo itemVenda) {
		this.item = itemVenda;
	}

	public BigDecimal getComissaoUnitaria() {
		return Optional.of(this.getValorVendaLiquido().multiply(BigDecimal.valueOf(item.getComissaoTx())))
				.orElse(BigDecimal.valueOf(0d));
	}

	public BigDecimal getComissaoTotal() {
		return this.getComissaoUnitaria().multiply(BigDecimal.valueOf(item.getQuantidade()));
	}

	public BigDecimal getLucroBrutoUnitario() {
		return this.valueOrZero(this.getValorVendaLiquido()).subtract(valueOrZero(item.getCusto()));
	}

	public BigDecimal getLucroBrutoTotal() {
		return this.valueOrZero(this.getLucroBrutoUnitario()).multiply(BigDecimal.valueOf(item.getQuantidade()));
	}

	public BigDecimal getLucroLiquidoUnitario() {
		return this.getLucroBrutoUnitario()
				.subtract(BigDecimal.valueOf(item.getComissaoTx()).multiply(this.getValorVendaLiquido()));
	}

	public BigDecimal getLucroLiquidoTotal() {
		return this.getLucroLiquidoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade()));
	}

	public BigDecimal getValorVendaLiquido() {
		return this.valueOrZero(item.getValorVendaBruto()).subtract(valueOrZero(item.getDesconto()));
	}

	public BigDecimal getValorVendaLiquidoTotal() {
		return getValorVendaLiquido().multiply(BigDecimal.valueOf(item.getQuantidade()));
	}

	public BigDecimal getValorTotalDesconto() {
		return this.valueOrZero(item.getDesconto()).multiply(BigDecimal.valueOf(item.getQuantidade()));
	}

	public BigDecimal getValorTotalBruto() {
		return valueOrZero(item.getValorVendaBruto()).multiply(BigDecimal.valueOf(item.getQuantidade()));
	}

	private BigDecimal valueOrZero(BigDecimal value) {
		return Optional.ofNullable(value).orElse(BigDecimal.valueOf(0));
	}

}
