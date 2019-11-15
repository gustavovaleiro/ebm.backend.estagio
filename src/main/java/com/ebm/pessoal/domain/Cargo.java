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
 
public class Cargo implements Serializable{
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

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
	
	

	public Cargo() {
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNomeCargo() {
		return nomeCargo;
	}
	public void setNomeCargo(String nomeCargo) {
		this.nomeCargo = nomeCargo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public BigDecimal getSalarioBase() {
		return salarioBase;
	}
	public void setSalarioBase(BigDecimal salarioBase) {
		this.salarioBase = salarioBase;
	}
	public HistoricoCadastral getHistorico() {
		return historico;
	}
	public void setHistorico(HistoricoCadastral historico) {
		this.historico = historico;
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
		Cargo other = (Cargo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
