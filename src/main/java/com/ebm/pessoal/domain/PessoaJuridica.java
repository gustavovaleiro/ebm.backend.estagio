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
	private String RazaoSocial;
	private String incricaoEstadual;
	private String inscricaoMunicipal;
	
	public PessoaJuridica() {}


	public PessoaJuridica(Integer id, String nome, String cnpj, String razaoSocial, String incricaoEstadual, String inscricaoMunicipal) {
		super(id, nome, TipoPessoa.PESSOAJURIDICA);
		this.cnpj = cnpj;
		RazaoSocial = razaoSocial;
		this.incricaoEstadual = incricaoEstadual;
		this.inscricaoMunicipal = inscricaoMunicipal;
	}


	public String getCnpj() {
		return cnpj;
	}


	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}


	public String getRazaoSocial() {
		return RazaoSocial;
	}


	public void setRazaoSocial(String razaoSocial) {
		RazaoSocial = razaoSocial;
	}


	public String getIncricaoEstadual() {
		return incricaoEstadual;
	}


	public void setIncricaoEstadual(String incricaoEstadual) {
		this.incricaoEstadual = incricaoEstadual;
	}


	public String getInscricaoMunicipal() {
		return inscricaoMunicipal;
	}


	public void setInscricaoMunicipal(String inscricaoMunicipal) {
		this.inscricaoMunicipal = inscricaoMunicipal;
	}
	
	
	
}
