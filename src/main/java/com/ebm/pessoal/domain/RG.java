package com.ebm.pessoal.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class RG implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column( length = 11, unique = true, name = "rg_numero")
	private String RG;
	
	@Column( length = 4, name = "rg_emissor")
	private String emissor;

	@ManyToOne
	@JoinColumn(name = "rg_uf")
	private Estado UF;
	
	
	public RG() {}
	
	public RG(String rG, String emissor, Estado uF) {
		super();
		RG = rG;
		this.emissor = emissor;
		UF = uF;
	}

	public String getRG() {
		return RG;
	}

	public void setRG(String rG) {
		RG = rG;
	}

	public String getEmissor() {
		return emissor;
	}

	public void setEmissor(String emissor) {
		this.emissor = emissor;
	}

	public Estado getUF() {
		return UF;
	}

	public void setUF(Estado uF) {
		UF = uF;
	}
	
	
}
