package com.ebm.estoque.domain;

import java.math.BigDecimal;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.ebm.estoque.domain.interfaces.ItemVendaInfo;
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
	private BigDecimal valor;
	@Column(nullable = false)
	private Integer quantidade;

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

	public Double getComissao() {
		return Optional.of(this.getProduto().getComissaoVenda()).orElse(0d);
	}

	@Transient
	@JsonIgnore
	public BigDecimal getLucroBrutoUnitario() {
		return this.valueOrZero(this.getValorVendaLiquido()).subtract(valueOrZero(this.id.getProduto().getCustoTotal()));
	}

	@Transient
	@JsonIgnore
	public BigDecimal getLucroBrutoTotal() {
		return this.valueOrZero(this.getLucroBrutoUnitario()).multiply(quantidadeOrZero());
	}

	private BigDecimal quantidadeOrZero() {
		return BigDecimal.valueOf(Optional.ofNullable(quantidade).orElse(0));
	}

	@Transient
	@JsonIgnore
	public BigDecimal getValorVendaLiquido() {
		return this.valueOrZero(this.valor).subtract(valueOrZero(this.desconto));
	}

	@Transient
	@JsonIgnore
	public BigDecimal getValorVendaTotal() {
		return getValorVendaLiquido().multiply(quantidadeOrZero());
	}

	@Transient
	@JsonIgnore
	public BigDecimal getValorTotalDesconto() {
		return this.valueOrZero(this.getDesconto()).multiply(this.quantidadeOrZero());
	}

	@Transient
	@JsonIgnore
	public BigDecimal getValorTotalSemDesconto() {
		return valueOrZero(this.valor).multiply(quantidadeOrZero());
	}

	@Transient
	@JsonIgnore
	public BigDecimal getLucroLiquidoUnitario() {
		return this.getLucroBrutoUnitario()
				.subtract(BigDecimal.valueOf(this.getComissao()).multiply(this.getValorVendaLiquido()));
	}

	@Transient
	@JsonIgnore
	public BigDecimal getLucroLiquidoTotal() {
		return this.getLucroLiquidoUnitario().multiply(quantidadeOrZero());
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
