package com.ebm.estoque.domain;

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
	
	
}
