package com.ebm.pessoal.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name ="pessoa_id")
public class PessoaJuridica extends Pessoa  {
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private String cnpj; 
	
	@Column(nullable = false)
	private String razaoSocial;
	private String inscricaoEstadual;
	private String inscricaoMunicipal;
	
	public PessoaJuridica() {
		super.setTipo(TipoPessoa.PESSOAJURIDICA);
	}


	public PessoaJuridica(Integer id, String nome, String cnpj, String razaoSocial, String incricaoEstadual, String inscricaoMunicipal) {
		super(id, nome, TipoPessoa.PESSOAJURIDICA);
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
	
	
	
}
