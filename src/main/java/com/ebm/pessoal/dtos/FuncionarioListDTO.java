package com.ebm.pessoal.dtos;

import com.ebm.pessoal.domain.Funcionario;
import com.ebm.pessoal.domain.TipoPessoa;

public class FuncionarioListDTO {

	private Integer id;
	private String matricula;
	private String nome;
	private String tipo;
	private String cargo;
	
	public FuncionarioListDTO() {}
	public FuncionarioListDTO(Integer id, String nome, TipoPessoa tipo, String matricula, String cargo) {
		this.id=id;
		this.nome=nome;
		this.tipo = tipo.getDescricao();
		this.matricula=matricula;
		this.cargo = cargo;
	}

	public FuncionarioListDTO(Funcionario f ) {
		this(f.getId(), f.getPessoa().getNome(), f.getPessoa().getTipo(), f.getMatricula(), f.getCargo().getNomeCargo());
	}
	

	public String getMatricula() {
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	public String getCargo() {
		return cargo;
	}
	public void setCargo(String cargo) {
		this.cargo = cargo;
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
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}
