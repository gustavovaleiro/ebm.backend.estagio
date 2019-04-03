package com.ebm.estoque.domain;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.ebm.comercial.domain.Venda;

@Entity
@DiscriminatorValue("S")
public class Saida extends Movimentacao {
	private static final long serialVersionUID = 1L;

	@OneToOne(targetEntity=Venda.class)
	private OrigemMovimentacao origemSaida;

	@OneToMany(mappedBy = "id.saida")
	private Set<ProdutoSaida> produtos = new HashSet<ProdutoSaida>();

	public Saida() {
		super();
	}

	public Saida(Integer id, String documento, String descricao, LocalDate dataMovimentacao, OrigemMovimentacao saida) {
		super(id, documento, descricao, dataMovimentacao);
		this.origemSaida = saida;
	}

	public double getValorTotal() {
		return produtos.stream().mapToDouble(x -> x.getSubTotal()).sum();
	}

	public double getLucroTotalEstimado() {
		return produtos.stream().mapToDouble(x -> x.getLucroTotalEstimado()).sum();
	}

	public OrigemMovimentacao getOrigemSaida() {
		return origemSaida;
	}

	public void setOrigemSaida(OrigemMovimentacao saidaInterna) {
		this.origemSaida = saidaInterna;
	}

	public Set<ProdutoSaida> getProdutos() {
		return produtos;
	}

	public void setProdutos(Set<ProdutoSaida> produtos) {
		this.produtos = produtos;
	}
}
