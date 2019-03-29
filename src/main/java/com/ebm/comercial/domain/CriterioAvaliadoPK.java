package com.ebm.comercial.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class CriterioAvaliadoPK implements Serializable{
	private static final long serialVersionUID = 1L;
	@ManyToOne
	private Criterio criterio;
	@ManyToOne
	private AvaliacaoServico avaliacao;
	
	public CriterioAvaliadoPK() {}

	public CriterioAvaliadoPK(Criterio criterio, AvaliacaoServico avaliacao) {
		super();
		this.criterio = criterio;
		this.avaliacao = avaliacao;
	}

	public Criterio getCriterio() {
		return criterio;
	}

	public void setCriterio(Criterio criterio) {
		this.criterio = criterio;
	}

	public AvaliacaoServico getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(AvaliacaoServico avaliacao) {
		this.avaliacao = avaliacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((avaliacao == null) ? 0 : avaliacao.hashCode());
		result = prime * result + ((criterio == null) ? 0 : criterio.hashCode());
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
		CriterioAvaliadoPK other = (CriterioAvaliadoPK) obj;
		if (avaliacao == null) {
			if (other.avaliacao != null)
				return false;
		} else if (!avaliacao.equals(other.avaliacao))
			return false;
		if (criterio == null) {
			if (other.criterio != null)
				return false;
		} else if (!criterio.equals(other.criterio))
			return false;
		return true;
	}
	
	
}
