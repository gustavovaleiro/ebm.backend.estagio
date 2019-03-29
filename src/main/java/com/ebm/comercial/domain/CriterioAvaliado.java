package com.ebm.comercial.domain;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class CriterioAvaliado {
	
	@EmbeddedId
	private CriterioAvaliadoPK id = new CriterioAvaliadoPK();
	@Column(nullable = false)
	private double nota;
	@Column(length = 400)
	private String comentario;
	
	public CriterioAvaliado(){}
	
	
	public CriterioAvaliado(Criterio criterio, AvaliacaoServico avaliacao, double nota, String comentario) {
		this.id.setAvaliacao(avaliacao);
		this.id.setCriterio(criterio);
		this.nota = nota;
		this.comentario = comentario;
	}


	public double getNota() {
		return nota;
	}
	public Criterio getCriterio() {
		return id.getCriterio();
	}

	public void setCriterio(Criterio criterio) {
		this.id.setCriterio(criterio);;
	}

	public AvaliacaoServico getAvaliacao() {
		return this.id.getAvaliacao();
	}

	public void setAvaliacao(AvaliacaoServico avaliacao) {
		this.id.setAvaliacao(avaliacao);
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	

}
