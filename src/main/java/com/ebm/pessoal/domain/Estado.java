package com.ebm.pessoal.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Estado implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "estado_id")
	private Integer id;
	
	@Column(nullable = false, length = 2, name = "estado_uf")
	private String uF;
	
	@Column(length = 20, name = "estado_nome")
	private String nome;
	
	public Estado() {}
	
	
	
	public Estado(Integer id, String uF, String nome) {
		super();
		this.id = id;
		this.uF = uF;
		this.nome = nome;
	}



	public Integer getId() {
		return id;
	}
	public Estado setId(Integer id) {
		this.id = id;
		return this;
	}
	
	public String getUF() {
		return uF;
	}
	public Estado setUF(String uF) {
		this.uF = uF;
		return this;
	}
	
	
	public String getNome() {
		return nome;
	}
	public Estado setNome(String nome) {
		this.nome = nome;
		return this;
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
		Estado other = (Estado) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}

