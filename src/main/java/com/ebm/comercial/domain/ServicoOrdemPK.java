package com.ebm.comercial.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import com.ebm.estoque.domain.Servico;

@Embeddable
public class ServicoOrdemPK implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@ManyToOne
	private Servico servico;
	@ManyToOne
	private OrdemServico ordem;
	
	public ServicoOrdemPK() {}

	public ServicoOrdemPK(Servico servico, OrdemServico ordem) {
		super();
		this.servico = servico;
		this.ordem = ordem;
	}

	public Servico getServico() {
		return servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}

	public OrdemServico getOrdemServico() {
		return ordem;
	}

	public void setOrdemServico(OrdemServico ordem) {
		this.ordem = ordem;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((servico == null) ? 0 : servico.hashCode());
		result = prime * result + ((ordem == null) ? 0 : ordem.hashCode());
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
		ServicoOrdemPK other = (ServicoOrdemPK) obj;
		if (servico == null) {
			if (other.servico != null)
				return false;
		} else if (!servico.equals(other.servico))
			return false;
		if (ordem == null) {
			if (other.ordem != null)
				return false;
		} else if (!ordem.equals(other.ordem))
			return false;
		return true;
	}
	
	
}
