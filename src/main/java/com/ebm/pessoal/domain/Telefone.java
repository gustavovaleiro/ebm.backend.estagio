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
public class Telefone implements Serializable, Principalizar {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;
	
	@Column(length = 3, nullable = false)
	private String dDD;
	
	@Column(length = 9, nullable = false)
	private String numero;
	
	private String tipo;
	
	private boolean principal;
	@Embedded
	private HistoricoCadastral historico= new HistoricoCadastral();
	
	@Override
	public String toString() {
		return this.tipo + ": ("+dDD+")"+ numero;
	}

	public Telefone(Integer id, String dDD, String numero, String tipo, boolean principal) {
		super();
		this.id = id;
		this.dDD = dDD;
		this.numero = numero;
		this.tipo = tipo;
		this.principal = principal;
	}
}
