package com.ebm.comercial.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import com.ebm.estoque.domain.Produto;

@Embeddable
public class ProdutoVendaAbertaPK implements Serializable{

	private static final long serialVersionUID = 1L;
	@ManyToOne
	private Produto produto;
	@ManyToOne
	private VendaAberta venda;
	
	public ProdutoVendaAbertaPK() {}

	public ProdutoVendaAbertaPK(Produto produto, VendaAberta venda) {
		super();
		this.produto = produto;
		this.venda = venda;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public VendaAberta getVenda() {
		return venda;
	}

	public void setVenda(VendaAberta venda) {
		this.venda = venda;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((produto == null) ? 0 : produto.hashCode());
		result = prime * result + ((venda == null) ? 0 : venda.hashCode());
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
		ProdutoVendaAbertaPK other = (ProdutoVendaAbertaPK) obj;
		if (produto == null) {
			if (other.produto != null)
				return false;
		} else if (!produto.equals(other.produto))
			return false;
		if (venda == null) {
			if (other.venda != null)
				return false;
		} else if (!venda.equals(other.venda))
			return false;
		return true;
	}
	
	
}
