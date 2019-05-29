package com.ebm.estoque.domain.enums;

import com.ebm.exceptions.DataIntegrityException;

import lombok.Getter;

@Getter
public enum TipoMovimentacao {
	ENTRADA(0, "Entrada"), SAIDA(1, "Saida");

	private Integer id;
	private String desc;

	private TipoMovimentacao(Integer id, String desc) {
		this.id = id;
		this.desc = desc;
	}

	public static TipoMovimentacao fromString(String tipo) {
		if (tipo.toLowerCase().contains("in") || tipo.toLowerCase().contains("en"))
			return TipoMovimentacao.ENTRADA;
		else if (tipo.toLowerCase().contains("out") || tipo.toLowerCase().contains("sa"))
			return TipoMovimentacao.SAIDA;
		else
			throw new DataIntegrityException("tipo invalido");
	}

}
