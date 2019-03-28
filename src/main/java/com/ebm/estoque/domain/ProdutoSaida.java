package com.ebm.estoque.domain;

import java.math.BigDecimal;

public class ProdutoSaida {
	
	private ProdutoSaidaPK id = new ProdutoSaidaPK();
	
	private BigDecimal valor_venda;
	
	private int quantidade;
	
	public ProdutoSaida() {}

	public ProdutoSaida(Produto produto, Saida saida,BigDecimal valor_venda, int quantidade) {
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
	public BigDecimal getValorUnitario() {
		return valor_venda;
	}

	public void setValorUnitario(BigDecimal valor_venda) {
		this.valor_venda = valor_venda;
	}
	
	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	
	public BigDecimal getLucroUnitarioEstimado() {
		return this.valor_venda.subtract(getProduto().getCustoTotal());
	}
	public BigDecimal getLucroTotalEstimado() {
		return this.getLucroUnitarioEstimado().multiply(BigDecimal.valueOf(quantidade));
	}

	public BigDecimal getSubTotal() {
		return valor_venda.multiply(BigDecimal.valueOf(quantidade));
	}
	
	
}
