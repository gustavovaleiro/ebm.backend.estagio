package com.ebm.pessoal.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class TipoTelefone implements Serializable{
	private static final long serialVersionUID = 1L;
	private String tipo;
	
	public TipoTelefone() {}

	public TipoTelefone(String tipo) {
		this.tipo = tipo;
	}
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
}
