package com.ebm.estoque.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class ProdutoMovimentacaoPK implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	private Produto produto;
	@ManyToOne
	private Movimentacao movimentacao;
	public ProdutoMovimentacaoPK(Produto produto, Movimentacao movimentacao) {
		this.produto = produto;
		this.movimentacao = movimentacao;
	}
	
	public ProdutoMovimentacaoPK() {}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Movimentacao getMovimentacao() {
		return movimentacao;
	}

	public void setMovimentacao(Movimentacao movimentacao) {
		this.movimentacao = movimentacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((movimentacao == null) ? 0 : movimentacao.hashCode());
		result = prime * result + ((produto == null) ? 0 : produto.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProdutoMovimentacaoPK other = (ProdutoMovimentacaoPK) obj;
		if (movimentacao == null) {
			if (other.movimentacao != null)
				return false;
		} else if (!movimentacao.equals(other.movimentacao))
			return false;
		if (produto == null) {
			if (other.produto != null)
				return false;
		} else if (!produto.equals(other.produto))
			return false;
		return true;
	}
	
	
}
