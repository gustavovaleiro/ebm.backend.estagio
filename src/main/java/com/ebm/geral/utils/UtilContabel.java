package com.ebm.geral.utils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;

import javax.persistence.Transient;

import com.ebm.estoque.domain.Item;
import com.ebm.estoque.domain.interfaces.ItemVendaInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UtilContabel {

	public static Double calculaComissaoTotal(Collection<ItemVendaInfo> item, double taxaComissaoA) {
		BigDecimal taxaComissao = BigDecimal.valueOf(Optional.of(taxaComissaoA).orElse(0d));
		return item.stream().mapToDouble(i -> i.getValorVendaLiquido().multiply(taxaComissao).multiply(BigDecimal.valueOf(i.getQuantidade())).doubleValue()).sum();
	}

	public static Double calculaComissaoTotal(Collection<ItemVendaInfo> item) {
		return item.stream().mapToDouble(i -> i.getCalculadora().getComissaoTotal().doubleValue()).sum();
	}
	 
	public static Double calcularLucroBruto(Collection<ItemVendaInfo> item) {
		return item.stream().mapToDouble( i -> i.getCalculadora().getLucroBrutoTotal().doubleValue()).sum();
	}

	public static Double calcularLucroLiquido(Collection<ItemVendaInfo> item) {
		 return item.stream().mapToDouble(i -> i.getCalculadora().getLucroLiquidoTotal().doubleValue()).sum();
	}
	public static Double calcularLucroLiquido(Collection<ItemVendaInfo> item, double taxaComissaoA) {
		 return UtilContabel.calcularLucroBruto(item) - UtilContabel.calculaComissaoTotal(item, taxaComissaoA);
	}
	 
	public static Double calcularCustoTotal(Collection<ItemVendaInfo> item) {
		return item.stream().mapToDouble(i-> i.getCusto().multiply(BigDecimal.valueOf(i.getQuantidade())).doubleValue()).sum();
	}
	 
	public static Double calcularPrecoValorVendaBrutoTotal(Collection<ItemVendaInfo> item) {
		return item.stream().mapToDouble(i -> i.getCalculadora().getValorTotalBruto().doubleValue()).sum();
	}
	 
	public static Double calcularPrecoValorVendaLiquidoTotal(Collection<ItemVendaInfo> item) {
		return item.stream().mapToDouble(i -> i.getCalculadora().getValorVendaLiquidoTotal().doubleValue()).sum();
	}
	
}
