package com.ebm.estoque.domain;

import java.math.BigDecimal;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.ebm.estoque.domain.interfaces.ItemVendaInfo;
import com.ebm.geral.utils.CalculosItemVenda;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoMovimentacao implements ItemVendaInfo {
	@EmbeddedId
	private ProdutoMovimentacaoPK id = new ProdutoMovimentacaoPK();

	private BigDecimal desconto;
	@Column(nullable = false)
	private BigDecimal valorVendaBruto;
	@Column(nullable = false)
	private Integer quantidade;
	@Transient
	@JsonIgnore
	final private CalculosItemVenda calculadora = new CalculosItemVenda(this);

	public ProdutoMovimentacao(Movimentacao m, Produto p, BigDecimal desconto, BigDecimal valorVendaBruto,
			Integer quantidade) {
		this.id = new ProdutoMovimentacaoPK(p, m);
		this.desconto = desconto;
		this.valorVendaBruto = valorVendaBruto;
		this.quantidade = quantidade;
	}

	public Produto getProduto() {
		return this.id.getProduto();
	}

	public void setProduto(Produto produto) {
		this.id.setProduto(produto);
	}

	public Movimentacao getMovimentacao() {
		return this.id.getMovimentacao();
	}

	public void setMovimentacao(Movimentacao movimentacao) {
		this.id.setMovimentacao(movimentacao);
	}

	@Transient
	@JsonIgnore
	public Double getComissaoTx() {
		return Optional.of(this.getProduto().getComissaoVenda()).orElse(0d);
	}

	@Override
	@Transient
	public BigDecimal getValorVendaLiquido() {
		return this.valueOrZero(this.valorVendaBruto).subtract(valueOrZero(this.desconto));
	}

	private BigDecimal valueOrZero(BigDecimal value) {
		return Optional.ofNullable(value).orElse(BigDecimal.valueOf(0));
	}

	@Transient
	@JsonIgnore
	@Override
	public BigDecimal getCusto() {
		return getProduto().getCustoTotal();
	}

}
