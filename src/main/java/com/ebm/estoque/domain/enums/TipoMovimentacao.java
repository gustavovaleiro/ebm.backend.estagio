package com.ebm.estoque.domain.enums;

public enum TipoMovimentacao {
	ENTRADA(0,"Entrada"),
	SAIDA(1, "Saida");
	
	private Integer id;
	private String desc;
	
	private TipoMovimentacao(Integer id, String desc) {
		this.id =id;
		this.desc=desc;
	}

	public Integer getId() {
		return id;
	}



	public String getDesc() {
		return desc;
	}

}

