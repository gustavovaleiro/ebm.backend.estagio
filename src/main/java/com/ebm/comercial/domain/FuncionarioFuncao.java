package com.ebm.comercial.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.ebm.pessoal.domain.Funcionario;
@Entity
public class FuncionarioFuncao  {
	
		
	@Column(nullable = false)
	private Funcionario funcionario;

	private String funcao;
	
	public FuncionarioFuncao() {
	}
	public FuncionarioFuncao(Funcionario funcionario, String funcao) {
		this.funcionario = funcionario;
		this.funcao = funcao;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}
	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
	public String getFuncao() {
		return funcao;
	}
	public void setFuncao(String funcao) {
		this.funcao = funcao;
	}
	
	@Id
	public Integer getId() {
		return this.hashCode();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((funcao == null) ? 0 : funcao.hashCode());
		result = prime * result + ((funcionario == null) ? 0 : funcionario.hashCode());
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
		FuncionarioFuncao other = (FuncionarioFuncao) obj;
		if (funcao == null) {
			if (other.funcao != null)
				return false;
		} else if (!funcao.equals(other.funcao))
			return false;
		if (funcionario == null) {
			if (other.funcionario != null)
				return false;
		} else if (!funcionario.equals(other.funcionario))
			return false;
		return true;
	}
	
	
	
	
	
	
}
