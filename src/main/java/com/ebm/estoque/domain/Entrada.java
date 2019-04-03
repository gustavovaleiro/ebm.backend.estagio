package com.ebm.estoque.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
@Entity
@DiscriminatorValue("E")
public class Entrada extends Movimentacao{
	private static final long serialVersionUID = 1L;
	
	@ManyToMany
	private List<Fornecedor> fornecedor = new ArrayList<>();
	
	@OneToMany(mappedBy="id.entrada")
	private Set<ProdutoEntrada> produtos = new HashSet<ProdutoEntrada>();
	
	public Entrada() {

	}

	public Entrada(Integer id, String documento, String descricao, LocalDate dataMovimentacao) {
		super(id, documento, descricao, dataMovimentacao);
		
	}

	

	public List<Fornecedor> getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(List<Fornecedor> fornecedor) {
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
