 package com.ebm.pessoal.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

import com.ebm.auth.Usuario;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Pessoa implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="pessoa_id")
	private Integer id;
	
	@Column(length = 60, nullable = false)
	private String nome;
	private LocalDateTime  dataCadastro;
	private Usuario usuarioCadastro;
	private LocalDateTime dataUltimaModificacao;
	private Usuario ultimaModificacao;
	@OneToMany(mappedBy = "pessoa")
	private List<Email> email = new ArrayList<Email>();
	@OneToMany(mappedBy = "pessoa")
	private List<Telefone> telefone = new ArrayList<Telefone>();
	@OneToMany(mappedBy = "pessoa")
	private List<Endereco> endereco = new ArrayList<Endereco>();
	private Integer tipo;
	
	public Pessoa() {}
	
	
	public Pessoa(Integer id, String nome, TipoPessoa tipo) {
		super();
		this.id = id;
		this.nome = nome;
		this.tipo = tipo.getCod();
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
	
	public void setTipo(TipoPessoa tipo) {
		this.tipo = tipo.getCod();
	}
	public TipoPessoa getTipo() {
		return TipoPessoa.toEnum(tipo);
	}
	public void setNome(String nome) {
		this.nome = nome;
	}


	public LocalDateTime getDataCadastro() {
		return dataCadastro;
	}


	public void setDataCadastro(LocalDateTime dataCadastro) {
		this.dataCadastro = dataCadastro;
	}


	public Usuario getUsuarioCadastro() {
		return usuarioCadastro;
	}


	public void setUsuarioCadastro(Usuario usuarioCadastro) {
		this.usuarioCadastro = usuarioCadastro;
	}


	public LocalDateTime getDataUltimaModificacao() {
		return dataUltimaModificacao;
	}


	public void setDataUltimaModificacao(LocalDateTime dataUltimaModificacao) {
		this.dataUltimaModificacao = dataUltimaModificacao;
	}


	public Usuario getUltimaModificacao() {
		return ultimaModificacao;
	}


	public void setUltimaModificacao(Usuario ultimaModificacao) {
		this.ultimaModificacao = ultimaModificacao;
	}


	public List<Email> getEmail() {
		return email;
	}


	public void setEmail(List<Email> email) {
		this.email = email;
	}


	public List<Telefone> getTelefone() {
		return telefone;
	}

	public void setTelefone(List<Telefone> telefone) {
		this.telefone = telefone;
	}


	public List<Endereco> getEndereco() {
		return endereco;
	}


	public void setEndereco(List<Endereco> endereco) {
		this.endereco = endereco;
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
		Pessoa other = (Pessoa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	
}
