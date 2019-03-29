package com.ebm.estoque.domain;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.OneToMany;

public class Entrada extends Movimentacao{
	private static final long serialVersionUID = 1L;
	
	private Fornecedor fornecedor;
	
	@OneToMany(mappedBy="id.entrada")
	private Set<ProdutoEntrada> produtos = new HashSet<ProdutoEntrada>();
	
	public Entrada() {
		super();
	}

	public Entrada(Integer id, String documento, String descricao, LocalDate dataMovimentacao, Fornecedor fornecedor) {
		super(id, documento, descricao, dataMovimentacao);
		this.fornecedor = fornecedor;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Set<ProdutoEntrada> getProdutos() {
		return produtos;
	}

	public void setProdutos(Set<ProdutoEntrada> produtos) {
		this.produtos = produtos;
	}
	
	public double getValorTotal() {
		 return produtos.stream().mapToDouble(x -> x.getSubTotal()).sum();
	}
	
}
