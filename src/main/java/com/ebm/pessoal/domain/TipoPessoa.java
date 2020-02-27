package com.ebm.pessoal.domain;

import com.ebm.geral.exceptions.DataIntegrityException;

public enum TipoPessoa {
	PESSOA_FISICA(0,"Pessoa Fisica"),
	PESSOA_JURIDICA(1, "Pessoa Juridica" );
	
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
	public static TipoPessoa fromString(String tipo) {
		if(tipo == null)
			return null;
		if(tipo.toLowerCase().contains("fisica") || tipo.toLowerCase().contains("fisica"))
			return TipoPessoa.PESSOA_FISICA;
		if(tipo.toLowerCase().contains("juridica") || tipo.toLowerCase().contains("juridica"))
			return TipoPessoa.PESSOA_JURIDICA;
		else
			throw new DataIntegrityException(DataIntegrityException.DEFAULT + ": tipo fornecido: " + tipo + "é invalido" );
		
	}

	
}
