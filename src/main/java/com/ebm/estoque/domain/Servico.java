package com.ebm.estoque.domain;

import java.math.BigDecimal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
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
	
	
	
}
