package com.ebm.pessoal.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Funcionario implements Serializable  {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	private Pessoa pessoa;
	
	@OneToOne
	@Column(nullable = false, name = "funcionario_cargo")
	private Cargo cargo;
	
	@Column( name = "funcionario_data_admissao")
	private LocalDate dataDeAdmissao;

	private BigDecimal comissao;
	
	private BigDecimal adicionalPessoal;
	
	private String matricula;
	
	public Funcionario(Integer id, Pessoa pessoa, String matricula, Cargo cargo, LocalDate dataDeAdmissao, BigDecimal comissao,
			BigDecimal adicionalPessoal) {
		this.id = id;
		this.pessoa = pessoa;
		this.cargo = cargo;
		this.dataDeAdmissao = dataDeAdmissao;
		this.comissao = comissao;
		this.adicionalPessoal = adicionalPessoal;
		this.matricula = matricula;
	}
	

	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	
	
	public String getMatricula() {
		return matricula;
	}


	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}


	public Cargo getCargo() {
		return cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	public LocalDate getDataDeAdmissao() {
		return dataDeAdmissao;
	}

	public void setDataDeAdmissao(LocalDate dataDeAdmissao) {
		this.dataDeAdmissao = dataDeAdmissao;
	}

	public BigDecimal getComissao() {
		return comissao;
	}

	public void setComissao(BigDecimal comissao) {
		this.comissao = comissao;
	}

	public BigDecimal getAdicionalPessoal() {
		return adicionalPessoal;
	}

	public void setAdicionalPessoal(BigDecimal adicionalPessoal) {
		this.adicionalPessoal = adicionalPessoal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pessoa == null) ? 0 : pessoa.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Funcionario other = (Funcionario) obj;
		if (pessoa == null) {
			if (other.pessoa != null)
				return false;
		} else if (!pessoa.equals(other.pessoa))
			return false;
		return true;
	}

	
	
	
}
