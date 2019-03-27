package com.ebm.auth;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class GrupoPermissaoPK implements Serializable{

	private static final long serialVersionUID = 1L;
	@ManyToOne
	private Grupo grupo;
	@ManyToOne
	private Permissao permissao;
	public GrupoPermissaoPK() {}
	
	public GrupoPermissaoPK(Grupo grupo, Permissao permissao) {
		this.grupo = grupo;
		this.permissao = permissao;
	}

	public Grupo getGrupo() {
		return grupo;
	}
	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}
	public Permissao getPermissao() {
		return permissao;
	}
	public void setPermissao(Permissao permissao) {
		this.permissao = permissao;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((grupo == null) ? 0 : grupo.hashCode());
		result = prime * result + ((permissao == null) ? 0 : permissao.hashCode());
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
		GrupoPermissaoPK other = (GrupoPermissaoPK) obj;
		if (grupo == null) {
			if (other.grupo != null)
				return false;
		} else if (!grupo.equals(other.grupo))
			return false;
		if (permissao == null) {
			if (other.permissao != null)
				return false;
		} else if (!permissao.equals(other.permissao))
			return false;
		return true;
	}

	
	
	
}
