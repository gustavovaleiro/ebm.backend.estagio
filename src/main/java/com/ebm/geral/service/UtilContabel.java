package com.ebm.geral.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;

import com.ebm.estoque.domain.Item;
import com.ebm.estoque.domain.interfaces.ItemVendaInfo;

public class UtilContabel {
	 
	public Double calculaComissaoTotal(Collection<ItemVendaInfo> item, Double taxaComissaoA) {
		BigDecimal taxaComissao = BigDecimal.valueOf(Optional.of(taxaComissaoA).orElse(0d));
		return item.stream().mapToDouble(i -> i.getValorVendaLiquido().multiply(taxaComissao).multiply(BigDecimal.valueOf(i.getQuantidade())).doubleValue()).sum();
	}


	 
	public Double calcularLucroBrutoEstimado(Collection<ItemVendaInfo> item) {
		return item.stream().mapToDouble( i -> i.getValorVendaLiquido().subtract(i.getCusto()).multiply(BigDecimal.valueOf(i.getQuantidade())).doubleValue()).sum();
	}

	 
	public Double calcularLucroLiquidoEstimado(Collection<ItemVendaInfo> item) {
		 return item.stream().mapToDouble(i -> i.getValorVendaLiquido().subtract(i.get) ).sum();
	}

	 
	public Double calcularLucroLiquidoEstimado(Collection<Item> item, Double taxaComissaoA) {
		BigDecimal taxaComissao = BigDecimal.valueOf(Optional.of(taxaComissaoA).orElse(0d));
		
		return item.stream().mapToDouble(i ->  i.getLucroEstimado().subtract(i.getPrecoVenda().multiply(taxaComissao)).doubleValue() ).sum();
	}

	 
	public Double calcularCustoTotal(Collection<Item> item) {
		return item.stream().mapToDouble(i-> i.getCustoTotal().doubleValue()).sum();
	}

	 
	public Double calcularPrecoVendaTotal(Collection<Item> item) {
		return item.stream().mapToDouble(i -> i. getPrecoVenda().doubleValue()).sum();
	}
	
	
}
