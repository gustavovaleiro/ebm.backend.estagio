package com.ebm.estoque.domain;

import java.time.LocalDate;
import java.util.List;

import com.ebm.auth.Usuario;
import com.ebm.pessoal.domain.Cliente;

public interface OrigemMovimentacao {
	public Integer getId();

	public Cliente getCliente();

	public Usuario getUsuario();

	//public List<FuncionarioFuncao> getFuncionariosComFuncao();

	public String getDescricao();
	
	public String getDocumento();
	public LocalDate getDataMovimentacao();
	public List<ProdutoMovimentacao> getProdutosMovimentacao();
}
