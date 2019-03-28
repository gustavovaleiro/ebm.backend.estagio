package com.ebm.estoque.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.OneToMany;

public class Saida extends Movimentacao {
	private static final long serialVersionUID = 1L;

	private Optional<OrigemSaida> origemSaida = Optional.empty();

	@OneToMany(mappedBy = "id.saida")
	private Set<ProdutoSaida> produtos = new HashSet<ProdutoSaida>();

	public Saida() {
		super();
	}

	public Saida(Integer id, String documento, String descricao, LocalDate dataMovimentacao, OrigemSaida saida) {
		super(id, documento, descricao, dataMovimentacao);
		this.origemSaida = Optional.of(saida);
	}

	public BigDecimal getValorTotal() {
		return BigDecimal.valueOf(produtos.stream().mapToDouble(x -> x.getSubTotal().doubleValue()).sum());
	}

	public BigDecimal getLucroTotalEstimado() {
		return BigDecimal.valueOf(produtos.stream().mapToDouble(x -> x.getLucroTotalEstimado().doubleValue()).sum());
	}

	public Optional<OrigemSaida> getOrigemSaida() {
		return origemSaida;
	}

	public void setOrigemSaida(OrigemSaida saidaInterna) {
		this.origemSaida = Optional.of(saidaInterna);
	}

	public Set<ProdutoSaida> getProdutos() {
		return produtos;
	}

	public void setProdutos(Set<ProdutoSaida> produtos) {
		this.produtos = produtos;
	}
}
