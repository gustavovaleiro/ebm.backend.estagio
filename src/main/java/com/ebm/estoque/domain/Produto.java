package com.ebm.estoque.domain;


import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.ebm.Utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@DiscriminatorValue("P")
public class Produto extends Item {
	private static final long serialVersionUID = 1L;
	

	private Integer estoqueMinimo;
	private Integer estoqueMax;
	private Integer estoqueAtual;
	private Double peso;
	private Double altura;
	private Double largura;
	private Double comprimento;

	
	@JsonIgnore
	@OneToMany(mappedBy="id.produto")
	private Set<ProdutoEntrada> entradas = new HashSet<>();
	@JsonIgnore
	@OneToMany(mappedBy="id.produto")
	private Set<ProdutoSaida> saidas = new HashSet<>();
	
	public static Produto of(String nome, Unidade un, CategoriaItem categoria) {
		return new Produto(null, nome, nome, un, categoria,  Utils.getRandomCodInterno(TipoItem.PRODUTO, nome), null, null, null, null, null, null, null);
	}
	
	public Produto() {	
		super.tipo = TipoItem.PRODUTO.getDescricao();
	}

	

	public Produto(Integer id, String nome, String descricao, Unidade unidade, CategoriaItem categoria,
			String codInterno, BigDecimal valorCompraMedio, BigDecimal outrasDespesa, Double margemLucro,
			Double comissaoVenda, Integer estoqueMinimo, Integer estoqueAtual, Integer estoqueMaximo) {
	
		super(id, nome, descricao, unidade, categoria, codInterno, valorCompraMedio, outrasDespesa, margemLucro, comissaoVenda);
		super.tipo = TipoItem.PRODUTO.getDescricao();
		this.estoqueMinimo = estoqueMinimo;
		this.estoqueAtual = estoqueAtual;
		this.estoqueMax = estoqueMaximo;
	}

	public Integer getEstoqueMinimo() {
		return estoqueMinimo;
	}

	public void setEstoqueMinimo(Integer estoqueMinimo) {
		this.estoqueMinimo = estoqueMinimo;
	}

	public Integer getEstoqueMax() {
		return estoqueMax;
	}

	public void setEstoqueMax(Integer estoqueMax) {
		this.estoqueMax = estoqueMax;
	}

	public Integer getEstoqueAtual() {
		return estoqueAtual;
	}

	public void setEstoqueAtual(Integer estoqueAtual) {
		this.estoqueAtual = estoqueAtual;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	public Double getAltura() {
		return altura;
	}

	public void setAltura(Double altura) {
		this.altura = altura;
	}

	public Double getLargura() {
		return largura;
	}

	public void setLargura(Double largura) {
		this.largura = largura;
	}

	public Double getComprimento() {
		return comprimento;
	}

	public void setComprimento(Double comprimento) {
		this.comprimento = comprimento;
	}

	
}
