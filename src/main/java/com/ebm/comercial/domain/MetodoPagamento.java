package com.ebm.comercial.domain;

public enum MetodoPagamento {
	DINHEIRO(1, "Pagamento com dinheiro em espécie"),
	CREDITO(2, "Pagamento com cartao de credito"),
	DEBITO(3, "Pagamento com cartao de debito"),
	CREDIARIO(4, "Pagamento com crediario proprio");
	
	private final int cod;
	private final String desc;
	MetodoPagamento(int cod, String desc) {
		this.cod = cod;
		this.desc = desc;
	}
	public String getDesc() {
		return desc;
	}
	public Integer getCod() {
		return cod;
	}
	
	public static OSEstado toEnum(Integer cod) {
		if(cod == null) {
			return null;
		}
		for(OSEstado x: OSEstado.values()) {
			if(cod.equals(x.getCod()))
				return x;
		}
		
		throw new IllegalArgumentException("id invalido: " + cod);
	}
}
