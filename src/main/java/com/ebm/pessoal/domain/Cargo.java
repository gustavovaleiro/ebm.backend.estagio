package com.ebm.pessoal.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
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
public class Cargo implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;
	@Column( nullable = false, length = 60)
	@NotNull(message = "O campo nome do cargo não pode ser nulo")
	@NotEmpty(message = "O campo nome do cargo não pode ser vazio")
	@Length(min = 3, max  = 60, message = "O campo nome do cargo deve ter entre 3 e 60 caracteres")
	private String nomeCargo;
	@Column
	@Length(min = 3, max  = 240, message = "O campo descrição do cargo deve ter entre 3 e 240 caracteres")
	private String descricao;
	@Column( nullable = false)
	@NotNull(message = "O campo salario base do cargo não pode ser nulo")
	@Min(value = 0, message = "O campo salario base do cargo não pode ser negativo")
	private BigDecimal salarioBase;
	@Embedded
	private HistoricoCadastral historico = new HistoricoCadastral();
	public Cargo(Integer id, String nomeCargo, BigDecimal salarioBase, String descricao) {
		this.id = id;
		this.nomeCargo = nomeCargo;
		this.salarioBase = salarioBase;
		this.descricao = descricao;
	}

}
