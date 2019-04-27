package com.ebm.pessoal.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import com.ebm.auth.Usuario;

@Entity
public class Funcionario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@MapsId
	@OneToOne
	@JoinColumn(nullable = false)
	private Pessoa pessoa;

	@OneToOne
	private Cargo cargo;

	@Column
	private LocalDate dataDeAdmissao;
	@OneToOne
	private Usuario usuario;

	private Double comissao;

	private BigDecimal adicionalPessoal;

	private String matricula;

	public Funcionario() {
	}

	public Funcionario(Integer id, Pessoa pessoa, String matricula, Cargo cargo, LocalDate dataDeAdmissao,
			Double comissao, BigDecimal adicionalPessoal) {
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

	public Double getComissao() {
		return comissao;
	}

	public void setComissao(Double comissao) {
		this.comissao = comissao;
	}

	public BigDecimal getAdicionalPessoal() {
		return adicionalPessoal;
	}

	public void setAdicionalPessoal(BigDecimal adicionalPessoal) {
		this.adicionalPessoal = adicionalPessoal;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
