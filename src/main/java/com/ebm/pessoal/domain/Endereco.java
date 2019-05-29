package com.ebm.pessoal.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Endereco implements Serializable, Principalizar {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;
	@Column(length = 40)
	private String rua;
	@Column(length = 20)
	private String bairro;
	@ManyToOne(optional = false)
	@JoinColumn(nullable = false, name = "endereco_cidade")
	private Cidade cidade;
	private String numero;
	@Column(length = 140)
	private String complemento;
	@Column(nullable = false, length = 8)
	private String CEP;
	private boolean principal;
	private String tipo;
	
}
