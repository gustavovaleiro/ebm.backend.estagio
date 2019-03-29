package com.ebm.estoque.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
@Entity
public class ProdutoEntrada {
	@EmbeddedId
	private ProdutoEntradaPK id = new ProdutoEntradaPK();
	@Column(nullable = false)
	private BigDecimal valorUnitario;
	@Column(nullable = false)
	private double quantidade;
	
	public ProdutoEntrada() {}

	public ProdutoEntrada(Produto produto, Entrada entrada,BigDecimal valorUnitario, double quantidade) {
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

	public double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(double quantidade) {
		this.quantidade = quantidade;
	}
	public double getSubTotal() {
		return valorUnitario.multiply(BigDecimal.valueOf(quantidade)).doubleValue();
		
	}

	
	
	
}
