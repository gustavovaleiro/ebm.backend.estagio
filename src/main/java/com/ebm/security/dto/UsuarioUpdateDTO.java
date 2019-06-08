package com.ebm.security.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.ebm.security.PermissaoE;
import com.ebm.security.Usuario;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioUpdateDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String login;
	private Set<Integer> permissoes = new HashSet<>();
	public UsuarioUpdateDTO(Integer id, String login, Set<PermissaoE> permissoes) {
		super();
		this.id = id;
		this.login = login;
		this.permissoes = permissoes.stream().map(p -> p.getId()).collect(Collectors.toSet());
	}
	
	public Set<PermissaoE> getPermissoes() {
		return permissoes.stream().map(p -> PermissaoE.toEnum(p)).collect(Collectors.toSet());
	}

	public void setPermissoes(Set<PermissaoE> permissao) {
		if(permissao == null) 
			this.permissoes = null;
		else
			this.permissoes = permissao.stream().map(p -> p.getId()).collect(Collectors.toSet());
	}

	public static UsuarioUpdateDTO fromUsuario(Usuario user) {
		return new UsuarioUpdateDTO(user.getId(), user.getLogin(), user.getPermissoes());
		
	}

	
	
}
