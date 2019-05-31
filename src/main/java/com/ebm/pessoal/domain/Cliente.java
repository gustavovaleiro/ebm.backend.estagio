package com.ebm.pessoal.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cliente implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;

	@MapsId
	@OneToOne
	@JoinColumn(nullable = false)
	private Pessoa pessoa;
	@Column(nullable = false)
	private BigDecimal limite_compra;

	@Column(length = 240)
	private String descricao;
	@Embedded
	private HistoricoCadastral historico= new HistoricoCadastral();
	public Cliente(Integer id, Pessoa pessoa, BigDecimal limite_compra, String descricao) {
		this.id = id;
		this.pessoa = pessoa;
		this.limite_compra = limite_compra;
		this.descricao = descricao;
	}
	
}
