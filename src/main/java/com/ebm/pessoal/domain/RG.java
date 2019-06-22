package com.ebm.pessoal.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

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

	@Column(length = 15, unique = true, name = "rg_numero")
	@NotNull(message = "O campo RG não pode ser nulo")
	@NotEmpty(message = "O campo RG não pode ser vazio")
	@Length(min=5, max = 15, message = "O campo RG deve possuir entre 5 e 15 caracteres")
	private String RG;

	@Column(length = 12, name = "rg_emissor")
	@NotNull(message = "O campo RG emissor não pode ser nulo")
	@NotEmpty(message = "O campo RG emissor não pode ser vazio")
	@Length(min=4, max = 12, message = "O campo RG emissor deve possuir entre 4 e 12 caracteres")
	private String emissor;

	@ManyToOne
	@JoinColumn(name = "rg_uf")
	@Valid
	private Estado UF;

}
