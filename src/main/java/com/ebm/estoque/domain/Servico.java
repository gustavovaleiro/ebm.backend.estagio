package com.ebm.estoque.domain;

import java.math.BigDecimal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.ebm.Utils;
@Entity
@DiscriminatorValue("S")
public class Servico extends Item{
	private static final long serialVersionUID = 1L;
	
	
	public Servico() {}


	public Servico(Integer id, String nome, String descricao, Unidade unidade, CategoriaItem categoria,
			String codInterno, BigDecimal valorCompraMedio, BigDecimal outrasDespesa, Double margemLucro,
			Double comissaoVenda) {
		super(id, nome, descricao, unidade, categoria, codInterno, valorCompraMedio, outrasDespesa, margemLucro, comissaoVenda);
	}

	@Override
	public String getTipo() {
		// TODO Auto-generated method stub
		return TipoItem.SERVICO.getDescricao();
	}


	public static Servico of(String nome, Unidade unidade, CategoriaItem categoria) {
		return new Servico(null, nome, nome, unidade, categoria, Utils.getRandomCodInterno(TipoItem.SERVICO, nome), null, null, null, null);
	}

	
	
	
}
