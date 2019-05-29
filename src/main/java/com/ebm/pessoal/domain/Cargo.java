package com.ebm.pessoal.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
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
public class Cargo implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;
	@Column( nullable = false, length = 44)
	private String nomeCargo;
	@Column
	private String descricao;
	@Column( nullable = false)
	private BigDecimal salarioBase;

	public Cargo(Integer id, String nomeCargo, BigDecimal salarioBase, String descricao) {
		this.id = id;
		this.nomeCargo = nomeCargo;
		this.salarioBase = salarioBase;
		this.descricao = descricao;
	}

}
