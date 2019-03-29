package com.ebm.comercial.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import com.ebm.estoque.domain.Produto;

@Entity
public class ProdutoVendaAberta {
	@EmbeddedId
	private ProdutoVendaAbertaPK id = new ProdutoVendaAbertaPK();
	@Column(nullable = false)
	private double quantidade;
	@Column(nullable = false)
	private BigDecimal valorVenda;
	private BigDecimal desconto;

	public ProdutoVendaAberta() {}

	public ProdutoVendaAberta(Produto produto, VendaAberta venda, double quantidade, BigDecimal valorVenda, BigDecimal desconto) {
		super();
		this.quantidade = quantidade;
		this.valorVenda = valorVenda;
		this.desconto = desconto;
		this.id.setProduto(produto);
		this.id.setVenda(venda);
	}
	
	public Produto getProduto() {
		return this.id.getProduto();
	}

	public void setProduto(Produto produto) {
		this.id.setProduto(produto);
	}

	public VendaAberta getVenda() {
		return this.id.getVenda();
	}

	public void setVenda(VendaAberta venda) {
		this.id.setVenda(venda);
	}

	public double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(double quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getValorVendaSemDesconto() {
		
		return valorVenda;
	}

	public void setValorVenda(BigDecimal valorVenda) {
		this.valorVenda = valorVenda;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}
	
	public double getSubTotal() {
		return getValorUnitarioComDesconto().multiply(BigDecimal.valueOf(quantidade)).doubleValue();
	}

	public BigDecimal getValorUnitarioComDesconto() {
		return valorVenda.subtract(desconto);
	}
	public double getLucroUnitario() {
		return getValorUnitarioComDesconto().multiply(BigDecimal.valueOf(quantidade)).doubleValue() - this.getProduto().getCustoTotal().doubleValue();
	}

}
