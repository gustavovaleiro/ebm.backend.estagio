package com.ebm.estoque.domain;

import java.util.List;
import java.util.Optional;

import com.ebm.auth.Usuario;
import com.ebm.comercial.domain.FuncionarioFuncao;
import com.ebm.pessoal.domain.Cliente;

public interface OrigemSaida {
	public Integer getId();

	public Optional<Cliente> getCliente();

	public Optional<Usuario> getUsuario();

	public Optional<List<FuncionarioFuncao>> getFuncionariosComFuncao();

	public Optional<String> getDescricao();
}
