package com.ebm.pessoal.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Endereco implements Serializable, Principalizar {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;
	@Column(length = 40)
	@NotNull(message = "O campo rua não pode ser nulo")
	@NotEmpty(message = "O campo rua não pode ser vazio")
	@Length(min =5, max = 60, message = "O campo rua deve possuir entre 5 e 60 caracteres")
	private String rua;
	@Column(length = 20)
	@NotNull(message = "O campo bairro não pode ser nulo")
	@NotEmpty(message = "O campo bairro não pode ser vazio")
	@Length(min =2, max = 60, message = "O campo bairro deve possuir entre 2 e 60 caracteres")
	private String bairro;
	@ManyToOne(optional = false)
	@JoinColumn(nullable = false, name = "endereco_cidade")
	@Valid
	private Cidade cidade;
	private String numero;
	@Column(length = 140)
	private String complemento;
	@Column(nullable = false, length = 11)
	@NotNull(message = "O campo CEP não pode ser nulo")
	@NotEmpty(message = "O campo CEP não pode ser vazio")
	@Length(min =8, max = 11, message = "O campo CEP deve possuir entre 8 e 11 caracteres")
	private String CEP;
	private boolean principal;
	private String tipo;
	@Embedded
    private HistoricoCadastral historico= new HistoricoCadastral();
	public Endereco(Integer id, String rua, String bairro, Cidade cidade, String numero, String complemento, String cEP,
			boolean principal, String tipo) {
		this.id = id;
		this.rua = rua;
		this.bairro = bairro;
		this.cidade = cidade;
		this.numero = numero;
		this.complemento = complemento;
		CEP = cEP;
		this.principal = principal;
		this.tipo = tipo;
	}
	
}
