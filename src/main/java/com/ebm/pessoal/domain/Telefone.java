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
public class Telefone implements Serializable, Principalizar {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;
	
	@Column(length = 3, nullable = false)
	@NotNull(message = "O campo ddd não pode ser nulo")
	@NotEmpty(message = "O campo ddd não pode ser vazio")
	@Length(min=2, max = 3, message = "O campo ddd  deve possuir entre 2 e 3  caracteres")
	private String ddd;
	
	@Column(length = 9, nullable = false)
	@NotNull(message = "O campo numero não pode ser nulo")
	@NotEmpty(message = "O campo numero não pode ser vazio")
	@Length(min=8, max = 9, message = "O campo numero deve possuir entre 8 e 9 caracteres")
	private String numero;
	
	private String tipo;
	
	private boolean principal;
	@Embedded
	private HistoricoCadastral historico= new HistoricoCadastral();
	
	@Override
	public String toString() {
		return this.tipo + ": ("+ddd+")"+ numero;
	}

	public Telefone(Integer id, String ddd, String numero, String tipo, boolean principal) {
		super();
		this.id = id;
		this.ddd = ddd;
		this.numero = numero;
		this.tipo = tipo;
		this.principal = principal;
	}
}
