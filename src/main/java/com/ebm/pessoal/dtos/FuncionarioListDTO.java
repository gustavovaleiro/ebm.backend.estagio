package com.ebm.pessoal.dtos;

import java.io.Serializable;

import com.ebm.pessoal.domain.Funcionario;
import com.ebm.pessoal.domain.TipoPessoa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioListDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String matricula;
	private String nome;
	private String tipo;
	private String cargo;
	private String telefone;
	private String email;

	public FuncionarioListDTO(Funcionario f ) {
		this(f.getId(), f.getPessoa().getNome(), f.getPessoa().getTipo().getDescricao(), f.getMatricula(), f.getCargo().getNomeCargo(), f.getPessoa().getTelefonePrincipal().toString(), f.getPessoa().getEmailPrincipal().toString());
	}
	
	
}
