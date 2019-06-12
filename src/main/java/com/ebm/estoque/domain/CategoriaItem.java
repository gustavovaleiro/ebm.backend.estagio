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

import org.hibernate.validator.constraints.Length;

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
public class CategoriaItem implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;
	
	@Column(nullable = false, length = 140)
	@NotNull(message ="Nome é um atributo requirido")
	@NotEmpty(message ="Nome é um atributo requirido")
	@Length(min = 2, max = 60, message ="O nome tem que possuir entre 2 e 60 caracteres")
	private String nome;
	
	@Embedded
	private HistoricoCadastral historico = new HistoricoCadastral();
	
	public CategoriaItem(Integer id, String nome) {

		this.id = id;
		this.nome = nome;
	}
	
}
