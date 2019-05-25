 package com.ebm.pessoal.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.ebm.security.Usuario;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public abstract class Pessoa implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="pessoa_id")
	private Integer id;
	
	@Column(length = 60, nullable = false)
	protected String nome;
	private LocalDateTime  dataCadastro;
	@ManyToOne
	private Usuario usuarioCadastro;
	private LocalDateTime dataUltimaModificacao;
	@ManyToOne
	private Usuario ultimaModificacao;
	@OneToMany
	@JoinColumn(name = "pessoa_id")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Email> email = new ArrayList<Email>();
	@OneToMany
	@JoinColumn(name = "pessoa_id")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Telefone> telefone = new ArrayList<Telefone>();
	@OneToMany
	@JoinColumn(name = "pessoa_id")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Endereco> endereco = new ArrayList<Endereco>();
	@Enumerated(EnumType.STRING )
	private TipoPessoa tipo;
	
	public Pessoa() {}
	
	
	public Pessoa(Integer id, String nome, TipoPessoa tipo) {
		super();
		this.id = id;
		this.nome = nome;
		this.tipo = tipo;
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
		this.tipo = tipo;
	}
	public TipoPessoa getTipo() {
		return this.tipo;
	}
	public Pessoa setNome(String nome) {
		this.nome = nome;
		return this;
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

	@Transient
	public Email getEmailPrincipal() {
		return email.stream().filter(e -> e.isPrincipal()).findAny().get();
	}
	@Transient
	public Telefone getTelefonePrincipal() {
		return telefone.stream().filter(e -> e.isPrincipal()).findAny().get();
	}
	@Transient
	public Endereco getEnderecoPrincipal() {
		return endereco.stream().filter(e -> e.isPrincipal()).findAny().get();
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


	public abstract String getDocument();


	
	
}
