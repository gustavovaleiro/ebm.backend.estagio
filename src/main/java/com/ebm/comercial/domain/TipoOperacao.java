package com.ebm.comercial.domain;

public enum TipoOperacao {
	SANGRIA("Consiste em retirar dinheiro do caixa"),
	REFORCO("Consiste em adicionar dinheiro a um caixa");
	
	private final String desc;
	TipoOperacao(String desc){
		this.desc=desc;
	}
	public String getDesc() {
		return desc;
	}
	
}
