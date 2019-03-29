package com.ebm.comercial.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import com.ebm.estoque.domain.Produto;

@Entity
public class ProdutoVenda {
	@EmbeddedId
	private ProdutoVendaPK id = new ProdutoVendaPK();
	@Column(nullable = false)
	private double quantidade;
	@Column(nullable = false)
	private BigDecimal valorVenda;
	private BigDecimal descontoUnitario =  new BigDecimal(0);
	public ProdutoVenda() {}
	
	public ProdutoVenda(Produto produto, Venda venda, int quantidade, BigDecimal valorVenda, BigDecimal descontoUnitario) {
		super();
		this.id.setProduto(produto); 
		this.id.setVenda(venda); 
		this.quantidade = quantidade;
		this.valorVenda = valorVenda;
		this.descontoUnitario = descontoUnitario;
	}

	public Produto getProduto() {
		return id.getProduto();
	}
	public Venda getVenda() {
		return id.getVenda();
	}
	public double getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(double quantidade) {
		this.quantidade = quantidade;
	}
	public double getValorVenda() {
		return valorVenda.doubleValue();
	}
	public void setValorVenda(BigDecimal valorVenda) {
		this.valorVenda = valorVenda;
	}
	public double getDescontoUnitario() {
		return descontoUnitario.doubleValue();
	}
	public void setDescontoUnitario(BigDecimal descontoUnitario) {
		this.descontoUnitario = descontoUnitario;
	}
	
	public double getSubTotal() {
		return valorVenda.subtract(descontoUnitario).multiply(BigDecimal.valueOf(quantidade)).doubleValue();
	}

	public double getSubLucroTotal() {
		return this.getSubTotal() - this.getProduto().getCustoTotal().multiply(BigDecimal.valueOf(quantidade)).doubleValue();
	}

}
