package com.ebm.pessoal.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name ="pessoa_id")
public class PessoaFisica extends Pessoa {
	
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 11)
	private String cpf; 
	private LocalDate dataNascimento;
	
	@Embedded
	private RG RG;
	private String nacionalidade;
	@ManyToOne
	private Cidade naturalidade;
	
	public PessoaFisica() {
		super.setTipo(TipoPessoa.PESSOAFISICA);
	}
	
	public PessoaFisica(Integer id, String nome, String cpf, LocalDate dataNascimento,RG rG, String nacionalidade, Cidade naturalidade) {
		super(id, nome, TipoPessoa.PESSOAFISICA);
		this.cpf = cpf;
		this.dataNascimento = dataNascimento;
		RG = rG;
		this.nacionalidade = nacionalidade;
		this.naturalidade = naturalidade;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public RG getRG() {
		return RG;
	}

	public void setRG(RG rG) {
		RG = rG;
	}

	public String getNacionalidade() {
		return nacionalidade;
	}

	public void setNacionalidade(String nacionalidade) {
		this.nacionalidade = nacionalidade;
	}

	public Cidade getNaturalidade() {
		return naturalidade;
	}

	public void setNaturalidade(Cidade naturalidade) {
		this.naturalidade = naturalidade;
	}
	
	
}
