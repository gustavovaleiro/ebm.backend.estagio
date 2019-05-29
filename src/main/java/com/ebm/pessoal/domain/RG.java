package com.ebm.pessoal.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RG implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(length = 11, unique = true, name = "rg_numero")
	private String RG;

	@Column(length = 4, name = "rg_emissor")
	private String emissor;

	@ManyToOne
	@JoinColumn(name = "rg_uf")
	private Estado UF;

}
