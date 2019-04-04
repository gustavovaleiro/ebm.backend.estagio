package com.ebm.pessoal.dtos;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;

import com.ebm.pessoal.domain.PessoaJuridica;

public class PessoaJuridicaUpdateDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	@NotNull(message = "Preenchimento Obrigatorio")
	@Length(min = 5, max = 80, message = "O tamanho deve ser entre 5 e 80 caracteres")
	private String nome;

	@NotNull(message = "Preenchimento Obrigatorio")
	@CNPJ
	private String cnpj;

	@NotNull(message = "Preenchimento Obrigatorio")
	private String razaoSocial;
	private String inscricaoEstadual;
	private String inscricaoMunicipal;
	
	public PessoaJuridicaUpdateDTO() {}
	public PessoaJuridicaUpdateDTO(PessoaJuridica pj) {
		this.id = pj.getId();
		this.nome= pj.getNome();
		this.cnpj = pj.getCnpj();
		this.razaoSocial = pj.getRazaoSocial();
		this.inscricaoEstadual = pj.getInscricaoEstadual();
		this.inscricaoMunicipal = pj.getInscricaoMunicipal();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
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
	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}
	public String getInscricaoMunicipal() {
		return inscricaoMunicipal;
	}
	public void setInscricaoMunicipal(String inscricaoMunicipal) {
		this.inscricaoMunicipal = inscricaoMunicipal;
	}
	

}
