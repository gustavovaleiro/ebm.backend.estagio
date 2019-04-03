package com.ebm.estoque.domain;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.ebm.auth.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@DiscriminatorValue("P")
public class Produto extends Item {
	private static final long serialVersionUID = 1L;
	
	@Column(nullable = false)
	private int estoqueMinimo;
	private int estoqueMax;
	private int estoqueAtual;
	private double peso;
	private double altura;
	private double largura;
	private double comprimento;
	
	@JsonIgnore
	@OneToMany(mappedBy="id.produto")
	private Set<ProdutoEntrada> entradas = new HashSet<>();
	@JsonIgnore
	@OneToMany(mappedBy="id.produto")
	private Set<ProdutoSaida> saidas = new HashSet<>();
	public Produto() {
	}

	public Produto(Integer id, String nome, String descricao, Unidade unidade, CategoriaItem categoria,
			String codInterno, double margemLucro, int estoqueMinimo, LocalDateTime dataCadastro, Usuario usuarioCadastro) {
		super(id, nome, descricao, unidade, categoria, codInterno, margemLucro, dataCadastro, usuarioCadastro);
		this.estoqueMinimo = estoqueMinimo;
	
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

	
	
}
