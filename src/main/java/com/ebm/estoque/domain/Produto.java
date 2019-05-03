package com.ebm.estoque.domain;


import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Random;
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
	}

	

	public Produto(Integer id, String nome, String descricao, Unidade unidade, CategoriaItem categoria,
			String codInterno, BigDecimal valorCompraMedio, BigDecimal outrasDespesa, Double margemLucro,
			Double comissaoVenda, Integer estoqueMinimo, Integer estoqueAtual, Integer estoqueMaximo) {
		super(id, nome, descricao, unidade, categoria, codInterno, valorCompraMedio, outrasDespesa, margemLucro, comissaoVenda);
		this.estoqueMinimo = estoqueMinimo;
		this.estoqueAtual = estoqueAtual;
		this.estoqueMax = estoqueMaximo;
	}



	public int getEstoqueMinimo() {
		return estoqueMinimo;
	}

	public void setEstoqueMinimo(int estoqueMinimo) {
		this.estoqueMinimo = estoqueMinimo;
	}

	public int getEstoqueMax() {
		return estoqueMax;
	}

	public void setEstoqueMax(int estoqueMax) {
		this.estoqueMax = estoqueMax;
	}

	public int getEstoqueAtual() {
		return estoqueAtual;
	}

	public void setEstoqueAtual(int estoqueAtual) {
		this.estoqueAtual = estoqueAtual;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	public double getAltura() {
		return altura;
	}

	public void setAltura(double altura) {
		this.altura = altura;
	}

	public double getLargura() {
		return largura;
	}

	public void setLargura(double largura) {
		this.largura = largura;
	}

	public double getComprimento() {
		return comprimento;
	}

	public void setComprimento(double comprimento) {
		this.comprimento = comprimento;
	}



	@Override
	public String getTipo() {
		return TipoItem.PRODUTO.getDescricao();
	}

	
	
}
