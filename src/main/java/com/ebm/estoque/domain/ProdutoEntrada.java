package com.ebm.estoque.domain;

import java.math.BigDecimal;

public class ProdutoEntrada {
	
	private ProdutoEntradaPK id = new ProdutoEntradaPK();
	
	private BigDecimal valorUnitario;
	
	private int quantidade;
	
	public ProdutoEntrada() {}

	public ProdutoEntrada(Produto produto, Entrada entrada,BigDecimal valorUnitario, int quantidade) {
		this.id.setProduto(produto);
		this.id.setEntrada(entrada);
		this.valorUnitario = valorUnitario;
		this.quantidade = quantidade;
	}
	
	public Produto getProduto() {
		return id.getProduto();
	}

	public void setProduto(Produto produto) {
		this.id.setProduto(produto);
	}

	public Entrada getEntrada() {
		return id.getEntrada();
	}

	public void setEntrada(Entrada entrada) {
		this.id.setEntrada(entrada);
	}
	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	public BigDecimal getSubTotal() {
		return valorUnitario.multiply(BigDecimal.valueOf(quantidade));
		
	}

	
	
	
}
