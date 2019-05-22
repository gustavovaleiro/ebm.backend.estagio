package com.ebm.auth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class Grupo implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false, length = 60)
	private String nome;
	
	@OneToMany
	@JoinColumn(name = "usuario_id")
	private List<Usuario>  usuarios = new ArrayList<Usuario>();
	
	@ElementCollection(fetch=FetchType.EAGER)
	private Set<PermissaoE> permissoes = new HashSet<>();
	
	public Grupo() {}

	public Grupo(Integer id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
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

	public List<Usuario> getUsuarios() {
		return Collections.unmodifiableList(usuarios);
	}

	public void addUsuario(Usuario usuario) {
		this.usuarios.add(usuario);
	}

	public Set<PermissaoE> getPermissoes() {
		return Collections.unmodifiableSet(permissoes);
	}

	public void setPermissao(Set<PermissaoE> permissao) {
		this.permissoes = permissao;
	}
	public void setUsuarios(List<Usuario> usersPersist) {
		this.usuarios = usersPersist;
		
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Grupo other = (Grupo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	
	
}
