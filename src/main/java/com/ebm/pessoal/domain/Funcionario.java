package com.ebm.pessoal.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

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
public class Funcionario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;
	
	@MapsId
	@OneToOne
	@JoinColumn(nullable = false)
	private Pessoa pessoa;
	private String matricula;
	@OneToOne
	private Cargo cargo;

	@Column
	private LocalDate dataDeAdmissao;

	private Double comissao;

	private BigDecimal adicionalPessoal;
	@Embedded
	private HistoricoCadastral historico= new HistoricoCadastral();
	public Funcionario(Integer id, Pessoa pessoa, String matricula, Cargo cargo, LocalDate dataDeAdmissao,
			Double comissao, BigDecimal adicionalPessoal) {
		this.id = id;
		this.pessoa = pessoa;
		this.matricula = matricula;
		this.cargo = cargo;
		this.dataDeAdmissao = dataDeAdmissao;
		this.comissao = comissao;
		this.adicionalPessoal = adicionalPessoal;
	}
	
}
