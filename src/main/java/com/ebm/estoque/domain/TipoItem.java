package com.ebm.estoque.domain;

import com.ebm.exceptions.DataIntegrityException;

public enum TipoItem {
	PRODUTO(1, "Produto"),
	SERVICO(2,"Servico");
	
	private int id;
	private String descricao;
	TipoItem(int id, String descricao){
		this.id = id;
		this.descricao = descricao;
	}
	public int getId() {
		return id;
	}
	public String getDescricao() {
		return descricao;
	}
	public Item getInstance() {
		if(this.id == 1)
			return new Produto();
		else
			return new Servico();
	}
	public static TipoItem fromString(String desc) {
		if(desc.toLowerCase().equals("produto"))
			return TipoItem.PRODUTO;
		else if (desc.toLowerCase().equals("servico"))
			return TipoItem.SERVICO;
		else
			throw new DataIntegrityException("descrição de tipo de item invalida");
	}
	
	
}
