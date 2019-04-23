package com.ebm.pessoal.domain;

public enum TipoPessoa {
	PESSOA_FISICA(0,"Pessoa Física"),
	PESSOA_JURIDICA(1, "Pessoa Jurídica" );
	
	private int cod;
	private String desc;

	
    TipoPessoa(int cod, String desc) {
		this.cod = cod;
		this.desc = desc;

	}
	public int getCod() {
		return cod;
	}
	public String getDescricao() {
		return desc;
	}

	
}
