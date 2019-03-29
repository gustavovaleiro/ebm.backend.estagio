package com.ebm.estoque.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
@Entity
public class ProdutoSaida {
	@EmbeddedId
	private ProdutoSaidaPK id = new ProdutoSaidaPK();
	
	@Column(nullable = false)
	private BigDecimal valor_venda;
	@Column(nullable = false)
	private double quantidade;
	
	public ProdutoSaida() {}

	public ProdutoSaida(Produto produto, Saida saida,BigDecimal valor_venda, double quantidade) {
		this.id.setProduto(produto);
		this.id.setSaida(saida);
		this.valor_venda = valor_venda;
		this.quantidade = quantidade;
	}
	
	public Produto getProduto() {
		return id.getProduto();
	}

	public void setProduto(Produto produto) {
		this.id.setProduto(produto);
	}

	public Saida getSaida() {
		return id.getSaida();
	}

	public void setSaida(Saida saida) {
		this.id.setSaida(saida);
	}
	public double getValorUnitario() {
		return valor_venda.doubleValue();
	}

	public void setValorUnitario(BigDecimal valor_venda) {
		this.valor_venda = valor_venda;
	}
	
	public double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(double quantidade) {
		this.quantidade = quantidade;
	}
	
	public double getLucroUnitarioEstimado() {
		return this.valor_venda.subtract(getProduto().getCustoTotal()).doubleValue();
	}
	public double getLucroTotalEstimado() {
		return BigDecimal.valueOf(this.getLucroUnitarioEstimado()).multiply(BigDecimal.valueOf(quantidade)).doubleValue();
	}

	public double getSubTotal() {
		return valor_venda.multiply(BigDecimal.valueOf(quantidade)).doubleValue();
	}
	
	
}
