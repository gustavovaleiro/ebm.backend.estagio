package com.ebm.pessoal.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import com.ebm.security.Usuario;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@PrimaryKeyJoinColumn(name ="pessoa_id")
@JsonTypeName("PessoaFisica")
public class PessoaFisica extends Pessoa {
	
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 11)
	private String cpf; 
	private LocalDate dataNascimento;
	
	@Embedded
	private RG rG;
	private String nacionalidade;
	@ManyToOne(optional = true)
	private Cidade naturalidade;
	
	public PessoaFisica() {
		super.setTipo(TipoPessoa.PESSOA_FISICA);
	}
	
	public PessoaFisica(Integer id, String nome, String cpf, LocalDate dataNascimento,RG rG, String nacionalidade, Cidade naturalidade) {
		super(id, nome, TipoPessoa.PESSOA_FISICA);
		this.cpf = cpf;
		this.dataNascimento = dataNascimento;
		this.rG = rG;
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
