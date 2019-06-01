package com.ebm.estoque.domain;

import java.math.BigDecimal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.ebm.estoque.domain.enums.TipoItem;
import com.ebm.geral.utils.Utils;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("S")
public class Servico extends Item {
	private static final long serialVersionUID = 1L;

	public Servico() {
		super.tipo = TipoItem.SERVICO.getDescricao();
	}

	public Servico(Integer id, String nome, String descricao, Unidade unidade, CategoriaItem categoria,
			String codInterno, BigDecimal valorCompraMedio, BigDecimal outrasDespesa, Double margemLucro,
			Double comissaoVenda) {
		super(id, nome, descricao, unidade, categoria, codInterno, valorCompraMedio, outrasDespesa, margemLucro,
				comissaoVenda);
		super.tipo = TipoItem.SERVICO.getDescricao();
	}

	public static Servico of(String nome, Unidade unidade, CategoriaItem categoria) {
		return new Servico(null, nome, nome, unidade, categoria, Utils.getRandomCodInterno(TipoItem.SERVICO, nome),
				null, null, null, null);
	}

}
