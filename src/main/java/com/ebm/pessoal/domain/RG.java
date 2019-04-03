package com.ebm.pessoal.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RG {
	
	@Column( length = 11)
	private String RG;
	
	@Column( length = 4)
	private String emissor;
	
	@Column(name="pessoa_fisica_RG_UF")
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
	
	
}
