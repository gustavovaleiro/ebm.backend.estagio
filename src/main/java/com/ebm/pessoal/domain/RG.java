package com.ebm.pessoal.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RG {
	
	@Column(name="pessoa_fisica_RG", length = 11)
	private String RG;
	
	@Column(name="pessoa_fisica_RG_EMISSOR", length = 4)
	private String Emissor;
	
	@Column(name="pessoa_fisica_RG_UF")
	private Estado UF;

	public String getRG() {
		return RG;
	}

	public void setRG(String rG) {
		RG = rG;
	}

	public String getEmissor() {
		return Emissor;
	}

	public void setEmissor(String emissor) {
		Emissor = emissor;
	}
	
	
}
