package com.ebm.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.ebm.pessoal.domain.HistoricoCadastral;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Grupo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;

	@Column(nullable = false, length = 60)
	private String nome;

	@OneToMany
	@JoinColumn(name = "usuario_id")
	private List<Usuario> usuarios = new ArrayList<Usuario>();

	@ElementCollection(fetch = FetchType.EAGER)
	private Set<Integer> permissoes = new HashSet<>();
	@Embedded
	private HistoricoCadastral historico= new HistoricoCadastral();
	
	public Grupo() {
	}

	public Grupo(Integer id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
	}

	public List<Usuario> getUsuarios() {
		return Collections.unmodifiableList(usuarios);
	}

	public void addUsuario(Usuario usuario) {
		if(usuario != null) {
			this.usuarios.add(usuario);
			if(usuario.getGrupo()!=this)
				usuario.setGrupo(this);
		}
		
		
		

	}

	public Set<PermissaoE> getPermissoes() {
		return permissoes.stream().map(p -> PermissaoE.toEnum(p)).collect(Collectors.toSet());
	}
	public void addPermissao(PermissaoE permissao) {
		this.permissoes.add(permissao.getId());
	}

	public void removePermissao(PermissaoE permissao) {
		this.permissoes.remove(permissao.getId());
	}

	public void setPermissao(Set<PermissaoE> permissao) {
		this.permissoes = permissao.stream().map(p -> p.getId()).collect(Collectors.toSet());
	}
	
	public void removeUsuario(Usuario usuario) {
		if(usuarios.contains(usuario))
			usuarios.remove(usuario);
		if(usuario.getGrupo() == this)
			usuario.setGrupo(null);
	}

}
