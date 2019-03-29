package com.ebm.auth;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.springframework.http.HttpMethod;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
public class GrupoPermissao {

	@JsonIgnore
	@EmbeddedId
	private GrupoPermissaoPK id  = new GrupoPermissaoPK();
	
	@ElementCollection
	private Set<HttpMethod> modos = new HashSet<>();
	public  GrupoPermissao() {}
	public GrupoPermissao(Grupo grupo, Permissao permissao) {
		this.id.setGrupo(grupo);
		this.id.setPermissao(permissao);
	}
	
	public Grupo getGrupo() {
		return this.id.getGrupo();
	}
	public Permissao getPermissao() {
		return this.id.getPermissao();
	}
	public void setGrupo(Grupo grupo) {
		this.id.setGrupo(grupo);
	}
	public void setPermissao(Permissao permissao) {
		this.id.setPermissao(permissao);
	}
	public Set<HttpMethod> getModos() {
		return Collections.unmodifiableSet(modos);
	}
	public void addModo(HttpMethod modo) {
		this.modos.add(modo);
	}
	public void removeMode(HttpMethod modo) {
		this.modos.remove(modo);
	}
	
	
	
}
