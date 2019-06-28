package com.ebm.security.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.ebm.security.PermissaoE;
import com.ebm.security.Usuario;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class UsuarioNewDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	@NotNull(message = "O login não pode ser nulo")
	@NotEmpty(message = "O login não pode ser vazio")
	@Length(min = 4, max = 20, message = "O login deve possuir entre 4 e 20 caracteres")
	private String login;
	@NotNull(message = "A senha não pode ser nula")
	@NotEmpty(message = "A senha não pode ser vazia")
	@Length(min = 6, max = 20, message = "A senha deve possuir entre 6 e 20 caracteres")
	private String senha;
	@NotNull(message = "As permissões não podem ser nulas")
	@NotEmpty(message = "As permissões não podem vazias")
	private Set<Integer> permissoes = new HashSet<>();
	@NotNull(message = "O funcionario não pode ser nulo")
	private Integer funcionario_id;

	public UsuarioNewDTO(String login, String senha, Set<PermissaoE> permissoes, Integer funcionario_id) {
		super();
		this.login = login;
		this.senha = senha;
		this.permissoes = permissoes.stream().map(p -> p.getId()).collect(Collectors.toSet());
		this.funcionario_id = funcionario_id;
	}

	public Set<PermissaoE> getPermissoes() {
		if(this.permissoes != null)
			return permissoes.stream().map(p -> PermissaoE.toEnum(p)).collect(Collectors.toSet());
		else
			return null;
	}

	public void setPermissoes(Set<PermissaoE> permissao) {
		if(permissao != null)
			this.permissoes = permissao.stream().map(p -> p.getId()).collect(Collectors.toSet());
		else
			permissao = null;
	}

	public static UsuarioNewDTO from(Usuario u) {
		return new UsuarioNewDTO(u.getLogin(), u.getSenha(), u.getPermissoes(), u.getFuncionario().getId());
	}

}
