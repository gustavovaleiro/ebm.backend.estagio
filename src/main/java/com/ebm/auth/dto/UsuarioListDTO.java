package com.ebm.auth.dto;

import java.io.Serializable;

import com.ebm.auth.Usuario;


public class UsuarioListDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String grupo;
	private String nome;
	private String login;
	private String email;
	
	public UsuarioListDTO() {}

	public UsuarioListDTO(Integer id, String grupo, String nome, String login, String email) {
		super();
		this.id = id;
		this.grupo = grupo;
		this.nome = nome;
		this.login = login;
		this.email = email;
	}
	
	public UsuarioListDTO(Usuario user) {
		this(user.getId(), user.getGrupo().getNome(), user.getFuncionario().getPessoa().getNome(), user.getLogin(), user.getEmailRecovery());
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
