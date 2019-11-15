package com.ebm.pessoal.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "pessoa_id")
@JsonTypeName("PESSOA_JURIDICA")
public class PessoaJuridica extends Pessoa {
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, unique = true)
	@NotNull(message = "O campo cnpj não pode ser nulo")
	@NotEmpty(message = "O campo cnpj não pode ser vazio")
	@Length(min =8, max = 16, message = "O campo cnpj deve possuir 8 ou 16 caracteres")
	private String cnpj;

	@Column(nullable = false)
	@NotNull(message = "O campo razaoSocial não pode ser nulo")
	@NotEmpty(message = "O campo razaoSocial não pode ser vazio")
	@Length(min =3, max = 80, message = "O campo razaoSocial deve possuir 3 ou 80 caracteres")
	private String razaoSocial;
	private String inscricaoEstadual;
	private String inscricaoMunicipal;

	public PessoaJuridica() {
		super.setTipo(TipoPessoa.PESSOA_JURIDICA);
	}

	public PessoaJuridica(Integer id, String nome, String cnpj, String razaoSocial, String incricaoEstadual,
			String inscricaoMunicipal) {
		super(id, nome, TipoPessoa.PESSOA_JURIDICA);
		this.cnpj = cnpj;
		this.razaoSocial = razaoSocial;
		this.inscricaoEstadual = incricaoEstadual;
		this.inscricaoMunicipal = inscricaoMunicipal;
	}

	public Pessoa withNome(String nome) {
		super.nome = nome;
		;
		return this;
	}

	public String getDocument() {
		return this.cnpj;
	}

}
