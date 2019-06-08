package com.ebm.security.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.ebm.security.PermissaoE;
import com.ebm.security.Usuario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class UsuarioNewDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private String login;
	private String senha;
	private Set<Integer> permissoes = new HashSet<>();
	private Integer funcionario_id;
	
	public UsuarioNewDTO(String login, String senha, Set<PermissaoE> permissoes, Integer funcionario_id) {
		super();
		this.login = login;
		this.senha = senha;
		this.permissoes = permissoes.stream().map(p -> p.getId()).collect(Collectors.toSet());
		this.funcionario_id = funcionario_id;
	}
	
	public Set<PermissaoE> getPermissoes() {
		return permissoes.stream().map(p -> PermissaoE.toEnum(p)).collect(Collectors.toSet());
	}

	public void setPermissoes(Set<PermissaoE> permissao) {
		this.permissoes = permissao.stream().map(p -> p.getId()).collect(Collectors.toSet());
	}

	public static UsuarioNewDTO fromUsuario(Usuario u) {
		return new UsuarioNewDTO(u.getLogin(), u.getSenha(), u.getPermissoes(), u.getFuncionario().getId());
	}


	
}
