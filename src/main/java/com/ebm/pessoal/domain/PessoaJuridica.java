package com.ebm.pessoal.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

import com.fasterxml.jackson.annotation.JsonTypeName;

@Entity
@PrimaryKeyJoinColumn(name ="pessoa_id")
@JsonTypeName("PessoaFisica")
public class PessoaJuridica extends Pessoa  {
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, unique = true)
	private String cnpj; 
	
	@Column(nullable = false)
	private String razaoSocial;
	private String inscricaoEstadual;
	private String inscricaoMunicipal;
	
	public PessoaJuridica() {
		super.setTipo(TipoPessoa.PESSOA_JURIDICA);
	}


	public PessoaJuridica(Integer id, String nome, String cnpj, String razaoSocial, String incricaoEstadual, String inscricaoMunicipal) {
		super(id, nome, TipoPessoa.PESSOA_JURIDICA);
		this.cnpj = cnpj;
		this.razaoSocial = razaoSocial;
		this.inscricaoEstadual = incricaoEstadual;
		this.inscricaoMunicipal = inscricaoMunicipal;
	}


	public String getCnpj() {
		return cnpj;
	}


	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}


	public String getRazaoSocial() {
		return razaoSocial;
	}


	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}


	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}


	public void setInscricaoEstadual(String incricaoEstadual) {
		this.inscricaoEstadual = incricaoEstadual;
	}


	public String getInscricaoMunicipal() {
		return inscricaoMunicipal;
	}


	public void setInscricaoMunicipal(String inscricaoMunicipal) {
		this.inscricaoMunicipal = inscricaoMunicipal;
	}


	public Pessoa withNome(String nome) {
		super.nome = nome;;
		return this;
	}
	
	
	
}
