package com.ebm.pessoal.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@PrimaryKeyJoinColumn(name ="pessoa_id")
@JsonTypeName("PESSOA_FISICA")
public class PessoaFisica extends Pessoa {
	
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 11, unique=true)	
	@NotNull(message = "O campo cpf não pode ser nulo")
	@NotEmpty(message = "O campo cpf não pode ser vazio")
	@Length(min =11, max =11, message = "O campo cpf deve possuir 11 caracteres")
	private String cpf; 
	@NotNull(message = "O campo dataNascimento não pode ser nulo")
	private LocalDate dataNascimento;
	
	@Embedded
	@NotNull(message = "O campo rG não pode ser nulo")
	@Valid
	private RG RG;
	private String nacionalidade;
	@ManyToOne(optional = true)
	@NotNull(message = "O campo naturalidade não pode ser nulo")
	@Valid
	private Cidade naturalidade;
	public PessoaFisica() {
		super.setTipo(TipoPessoa.PESSOA_FISICA);
	}
	
	public PessoaFisica(Integer id, String nome, String cpf, LocalDate dataNascimento,RG rG, String nacionalidade, Cidade naturalidade) {
		super(id, nome, TipoPessoa.PESSOA_FISICA);
		this.cpf = cpf;
		this.dataNascimento = dataNascimento;
		this.RG = rG;
		this.nacionalidade = nacionalidade;
		this.naturalidade = naturalidade;
	}
	
	public String getDocument() {
		return this.getCpf();
		
	}
	public Pessoa withNome(String nome) {
		// TODO Auto-generated method stub
		super.nome = nome;
		return this;
	}
	
	
}
