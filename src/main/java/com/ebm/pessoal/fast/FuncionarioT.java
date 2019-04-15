package com.ebm.pessoal.fast;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

@Entity
public class FuncionarioT implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	private Integer id;
	
	@ManyToOne
	private CargoT cargo;
	@MapsId
	@OneToOne
	private PessoaT pessoa;

	
	public FuncionarioT() {}

	public FuncionarioT(Integer id, CargoT cargo, PessoaT pessoa) {
		super();
		this.id = id;
		this.cargo = cargo;
		this.pessoa = pessoa;

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public CargoT getCargo() {
		return cargo;
	}

	public void setCargo(CargoT cargo) {
		this.cargo = cargo;
	}

	public PessoaT getPessoa() {
		return pessoa;
	}

	public void setPessoa(PessoaT pessoa) {
		this.pessoa = pessoa;
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
		FuncionarioT other = (FuncionarioT) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	

}
