package com.ebm.estoque.domain;

import java.math.BigDecimal;

public class ProdutoMovimentacao {
	private Produto produto;
	private double quantidade;
	private BigDecimal valor;
	
	public ProdutoMovimentacao() {}
	public ProdutoMovimentacao(Produto produto, double quantidade, BigDecimal valor) {
		super();
		this.produto = produto;
		this.quantidade = quantidade;
		this.valor = valor;
	}
	public Produto getProduto() {
		return produto;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	public double getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(double quantidade) {
		this.quantidade = quantidade;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	
}
