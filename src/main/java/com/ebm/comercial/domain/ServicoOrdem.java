package com.ebm.comercial.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import com.ebm.estoque.domain.Servico;

@Entity
public class ServicoOrdem {
	@EmbeddedId
	private ServicoOrdemPK id = new ServicoOrdemPK();
	@Column(nullable = false)
	private double quantidade;
	@Column(nullable = false)
	private BigDecimal valorVenda;
	private BigDecimal descontoUnitario =  new BigDecimal(0);
	public ServicoOrdem() {}
	
	public ServicoOrdem(Servico serv, OrdemServico ordem, int quantidade, BigDecimal valorVenda, BigDecimal descontoUnitario) {
		super();
		this.id.setServico(serv); 
		this.id.setOrdemServico(ordem); 
		this.quantidade = quantidade;
		this.valorVenda = valorVenda;
		this.descontoUnitario = descontoUnitario;
	}

	public Servico getServico() {
		return id.getServico();
	}
	public OrdemServico getOrdem() {
		return id.getOrdemServico();
	}
	public Double getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(Double quantidade) {
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

	public Double getLucro() {
		return getSubTotal() - getServico().getCustoTotal().multiply(BigDecimal.valueOf(quantidade)).doubleValue();
	}

}
