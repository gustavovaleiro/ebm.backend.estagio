package com.ebm.estoque.domain.enums;

import com.ebm.estoque.domain.Item;
import com.ebm.estoque.domain.Produto;
import com.ebm.estoque.domain.Servico;
import com.ebm.exceptions.DataIntegrityException;

import lombok.Getter;
@Getter
public enum TipoItem {
	PRODUTO(1, "Produto"),
	SERVICO(2,"Servico");
	
	private int id;
	private String descricao;
	TipoItem(int id, String descricao){
		this.id = id;
		this.descricao = descricao;
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
