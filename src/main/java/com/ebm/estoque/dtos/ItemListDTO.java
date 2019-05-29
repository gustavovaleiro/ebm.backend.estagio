package com.ebm.estoque.dtos;

import java.io.Serializable;

import com.ebm.estoque.domain.Item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemListDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String codInterno;
	private String tipo;
	private String nome;
	private String descricao;
	private String unidade;
	private String categoria;

	public ItemListDTO(Item item) {

		this(item.getId(), item.getCodInterno(), item.getTipo(), item.getNome(), item.getDescricao(),
				item.getUnidade().getAbrev(), item.getCategoria().getNome());
	}

}
