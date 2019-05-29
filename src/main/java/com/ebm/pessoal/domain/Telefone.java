package com.ebm.pessoal.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
	
	@Override
	public String toString() {
		return this.tipo + ": ("+dDD+")"+ numero;
	}
}
