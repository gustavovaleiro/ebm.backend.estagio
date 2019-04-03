package com.ebm.auth;

import java.util.Collections;
import java.util.Set;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.springframework.http.HttpMethod;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
public class GrupoPermissao {

	@JsonIgnore
	@EmbeddedId
	private GrupoPermissaoPK id  = new GrupoPermissaoPK();
	
	private boolean read;
	private boolean write;
	
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

	public boolean podeLer() {
		return read;
	}
	public void setRead(boolean read) {
		this.read = read;
	}
	public boolean podeEscrever() {
		return write;
	}
	public void setWrite(boolean write) {
		this.write = write;
	}
	
	
	
	
}
