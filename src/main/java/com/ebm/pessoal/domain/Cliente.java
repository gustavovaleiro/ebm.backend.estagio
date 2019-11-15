package com.ebm.pessoal.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;
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
public class Cliente implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;

	@MapsId
	@OneToOne
	@JoinColumn(nullable = false)
	@NotNull(message = "O campo pessoa não pode ser nulo")
	private Pessoa pessoa;
	@Column(nullable = false)
	@NotNull(message = "O campo limite de compra não pode ser nulo")
	@Min(value = 0, message = "O campo limite de compra não pode ser negativo")
	private BigDecimal limiteCompra;

	@Column(length = 480)
	@Length(min = 0, max = 480, message = "O campo descricao pode ter no maximo 480 caracteres")
	private String descricao;
	@Embedded
	private HistoricoCadastral historico= new HistoricoCadastral();
	public Cliente(Integer id, Pessoa pessoa, BigDecimal limiteCompra, String descricao) {
		this.id = id;
		this.pessoa = pessoa;
		this.limiteCompra = limiteCompra;
		this.descricao = descricao;
	}
	
}
