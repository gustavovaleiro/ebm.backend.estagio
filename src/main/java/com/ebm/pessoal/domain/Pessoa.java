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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public abstract class Pessoa implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="pessoa_id")
	@EqualsAndHashCode.Include
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
	

	public Pessoa(Integer id, String nome, TipoPessoa tipo) {
		super();
		this.id = id;
		this.nome = nome;
		this.tipo = tipo;
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

	public abstract String getDocument();
	
}
