package com.ebm.pessoal.dtos;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

import com.ebm.pessoal.domain.Cidade;
import com.ebm.pessoal.domain.PessoaFisica;
import com.ebm.pessoal.domain.RG;

public class PessoaFisicaUpdateDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	@NotNull(message="Preenchimento Obrigatorio")
	@Length(min=5,max=80, message = "O tamanho deve ser entre 5 e 80 caracteres")
	private String nome;
	
	@NotNull(message="cpf deve ser informado")
	@CPF
	private String cpf; 
	
	private LocalDate dataNascimento;
	
	private RG RG;
	
	private String nacionalidade;
	
	private Cidade naturalidade;
	
	public PessoaFisicaUpdateDTO() {}
	
	public PessoaFisicaUpdateDTO(PessoaFisica pf) {
		this.id = pf.getId();
		this.nome = pf.getNome();
		this.cpf = pf.getCpf();
		this.dataNascimento = pf.getDataNascimento();
		this.RG = pf.getRG();
		this.nacionalidade = pf.getNacionalidade();
		this.naturalidade = pf.getNaturalidade();
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
