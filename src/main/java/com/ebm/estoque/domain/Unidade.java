package com.ebm.estoque.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.ebm.pessoal.domain.HistoricoCadastral;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Unidade implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;

	@Column(nullable = false, length = 3)
	@NotNull(message = "A abreviação de uma unidade não pode ser nula")
	@NotEmpty
	private String abrev;

	@Column(nullable = false, length = 40)
	@NotNull(message = "O nome de uma unidade não pode ser nulo")
	private String nome;
	
	@Embedded
	private HistoricoCadastral historico= new HistoricoCadastral();

	public Unidade(Integer id, String abrev, String nome) {
		super();
		this.id = id;
		this.abrev = abrev;
		this.nome = nome;
	}

}
