package com.ebm.estoque.domain.interfaces;

import java.time.LocalDate;
import java.util.List;

import com.ebm.estoque.domain.ProdutoMovimentacao;
import com.ebm.pessoal.domain.Cliente;
import com.ebm.security.Usuario;

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
