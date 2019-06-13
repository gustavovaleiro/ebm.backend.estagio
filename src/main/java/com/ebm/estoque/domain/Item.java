package com.ebm.estoque.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.ebm.geral.utils.UtilNumeric;
import com.ebm.pessoal.domain.HistoricoCadastral;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "_tipo", discriminatorType = DiscriminatorType.STRING, length = 1)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipo")
public abstract class Item implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;

	@Column(nullable = false, length = 160)
	@NotNull(message = "Nome é um campo requirido")
	@Length(min = 2, max = 160, message = "O nome tem que possuir entre 2 e 160 caracteres")
	private String nome;

	@Column(nullable = false, length = 600)
	@NotNull(message = "Descrição é um campo requirido")
	@Length(min = 2, max = 600, message = "A Descrição tem que possuir entre 2 e 600 caracteres")
	private String descricao;

	@ManyToOne()
	@JoinColumn(nullable = false)
	@NotNull(message = "Unidate é um campo obrigatorio")
	private Unidade unidade;

	@ManyToOne()
	@JoinColumn(nullable = false)
	@NotNull(message = "Categoria é um campo obrigatorio")
	private CategoriaItem categoria;

	@Column(length = 60)
	@Length(max = 60, message = " O código interno deve possuir no máximo 60 caracteres")
	private String codInterno;
	
	@NotNull(message = "Valor de compra médio é um campo obrigatorio")
	@DecimalMin(value = "0", message = "O valor de compra médio não pode ser menor do que zero")
	private BigDecimal valorCompraMedio;
	
	@DecimalMin(value = "0", message = "Outras despesas não pode ser menor do que zero")
	private BigDecimal outrasDespesa;
	
	@NotNull(message = "Margem de lucro é um campo obrigatorio")
	@DecimalMin(value = "0", message = "Margem de lucro não pode ser menor do que zero")
	private Double margemLucro;
	
	@NotNull(message = "Comissao de venda é um campo obrigatorio")
	@DecimalMin(value = "0", message = "Comissao de venda não pode ser menor do que zero")
	private Double comissaoVenda;
	protected String tipo;
	
	@Embedded
	private HistoricoCadastral historico = new HistoricoCadastral();

	public Item(Integer id, String nome, String descricao, Unidade unidade, CategoriaItem categoria, String codInterno,
			BigDecimal valorCompraMedio, BigDecimal outrasDespesa, Double margemLucro, Double comissaoVenda) {
		super();
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.unidade = unidade;
		this.categoria = categoria;
		this.codInterno = codInterno;
		this.valorCompraMedio = valorCompraMedio;
		this.outrasDespesa = outrasDespesa;
		this.margemLucro = margemLucro;
		this.comissaoVenda = comissaoVenda;

	}

	@Transient
	public BigDecimal getPrecoVenda() {
		return (getCustoTotal()).multiply(BigDecimal.valueOf(UtilNumeric.valueOrZero(this.margemLucro) + 1d));
	}

	@Transient
	public BigDecimal getLucroEstimado() {
		return getCustoTotal().multiply(BigDecimal.valueOf(UtilNumeric.valueOrZero(this.margemLucro)));
	}

	@Transient
	public BigDecimal getComissaoEstimada() {
		return getPrecoVenda().multiply(BigDecimal.valueOf(UtilNumeric.valueOrZero(comissaoVenda)));
	}

	@Transient
	public Double getComissaoVenda() {
		return this.comissaoVenda;
	}

	@Transient
	public BigDecimal getCustoTotal() {
		return UtilNumeric.valueOrZero(this.getValorCompraMedio()).add(UtilNumeric.valueOrZero(this.outrasDespesa));
	}

}
