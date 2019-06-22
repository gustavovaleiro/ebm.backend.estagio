package com.ebm.pessoal.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Email implements Serializable, Principalizar{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;
	
	@Column(nullable = false, length = 60, unique = true)
	@NotNull(message = "O campo email não pode ser nulo")
	@NotEmpty(message = "O campo email não pode ser vazio")
	@Length(min=6, max = 60, message = "O campo email deve possuir entre 6 e 60 caracteres")
	private String email;
	
	@Column(length = 140, name = "email_descricao")
	@Length(min=1, max = 60, message = "O campo tipo deve possuir entre 6 e 60 caracteres")
	private String tipo;	
	private boolean principal;
	@Embedded
	private HistoricoCadastral historico= new HistoricoCadastral();
	public Email(Integer id, String email, String tipo, boolean principal) {
		super();
		this.id = id;
		this.email = email;
		this.tipo = tipo;
		this.principal = principal;
	}
	
}
