package com.ebm.estoque.domain;

import java.util.List;

import com.ebm.auth.Usuario;
import com.ebm.comercial.domain.FuncionarioFuncao;
import com.ebm.pessoal.domain.Cliente;

public interface OrigemSaida {
	public Integer getId();

	public Cliente getCliente();

	public Usuario getUsuario();

	public List<FuncionarioFuncao> getFuncionariosComFuncao();

	public String getDescricao();
}
