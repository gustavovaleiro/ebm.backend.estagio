package com.ebm.security.dto;

import java.io.Serializable;

import com.ebm.security.Usuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioListDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String cargo;
	private String nome;
	private String login;
	private String email;
	
	public UsuarioListDTO() {}

	public UsuarioListDTO(Integer id, String cargo, String nome, String login, String email) {
		super();
		this.id = id;
		this.cargo = cargo;
		this.nome = nome;
		this.login = login;
		this.email = email;
	}
	
	public UsuarioListDTO(Usuario user) {
		this(user.getId(), user.getFuncionario().getCargo().getNomeCargo(), user.getFuncionario().getPessoa().getNome(), user.getLogin(), user.getEmail());
	}

	
}
