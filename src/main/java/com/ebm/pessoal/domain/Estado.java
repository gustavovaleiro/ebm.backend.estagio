package com.ebm.pessoal.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

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
public class Estado implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "estado_id")
	@EqualsAndHashCode.Include
	private Integer id;

	@Column(nullable = false, length = 2, name = "estado_uf")
	@NotNull(message = "O campo estado uf não pode ser nulo")
	@NotEmpty(message = "O campo estado uf não pode ser vazio")
	@Length(min =2, max = 2, message = "O campo estado deve possuir 2caracteres")
	private String uf;

	@Column(length = 20, name = "estado_nome")
	@NotNull(message = "O campo estado nome não pode ser nulo")
	@NotEmpty(message = "O campo estado nome não pode ser vazio")
	@Length(min =3, max = 20, message = "O campo estado deve possuir 3 ou 20 caracteres")
	private String nome;
	

}
