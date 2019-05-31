package com.ebm.pessoal.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
	private String email;
	
	@Column(length = 140, name = "email_descricao")
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
