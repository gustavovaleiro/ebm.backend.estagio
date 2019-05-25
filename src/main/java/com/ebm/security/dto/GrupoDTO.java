package com.ebm.security.dto;

import java.io.Serializable;

import com.ebm.security.Grupo;

public class GrupoDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String nome;
	
	public GrupoDTO() {}
	public GrupoDTO(Integer id, String nome) {
		this.id = id;
		this.nome = nome;
	}
	public GrupoDTO(Grupo grupo) {
		this(grupo.getId(), grupo.getNome());
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

	
}
