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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
public class Funcionario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;

	@MapsId
	@OneToOne
	@JoinColumn(nullable = false)
	@NotNull(message = "O campo pessoa nao pode ser nulo")
	private Pessoa pessoa;
	@Length(min = 3, max = 60, message = "O campo matricula deve ter entre 3 e 20 caracteres")
	private String matricula;
	@OneToOne
	@NotNull(message = "O campo cargo nao pode ser nulo")
	private Cargo cargo;
	
	@Column
	@NotNull(message = "O campo dataDeAdmissao não pode ser nulo")
	private LocalDate dataDeAdmissao;
	@NotNull(message = "O campo comissão não pode ser nulo")
	@Min(value = 0, message = "O campo comissão deve possuir um valor entre 0 e 1")
	@Max(value = 1, message = "O campo comissão deve possuir um valor entre 0 e 1")
	private BigDecimal comissao;
	@NotNull(message = "O campo adicionalPessoal não pode ser nulo")
	@Min(value = 0, message = "O campo adicionalPessoal deve possuir um valor  que 0")
	private BigDecimal adicionalPessoal;
	@Embedded
	private HistoricoCadastral historico = new HistoricoCadastral();

	public Funcionario(Integer id, Pessoa pessoa, String matricula, Cargo cargo, LocalDate dataDeAdmissao,
			Double comissao, BigDecimal adicionalPessoal) {
		this.id = id;
		this.pessoa = pessoa;
		this.matricula = matricula;
		this.cargo = cargo;
		this.dataDeAdmissao = dataDeAdmissao;
		this.comissao = BigDecimal.valueOf(comissao);
		this.adicionalPessoal = adicionalPessoal;
	}
	
	public void setComissao(Double comissao) {
		if(comissao == null)
			this.comissao = null;
		else
			this.comissao = BigDecimal.valueOf(comissao);
	}
	public Double getComissao() {
		if(comissao == null)
			return  null;
		else
			return this.comissao.doubleValue();
		
	}


}
