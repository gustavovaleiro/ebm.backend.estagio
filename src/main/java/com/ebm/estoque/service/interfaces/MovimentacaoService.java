package com.ebm.estoque.service.interfaces;

import org.springframework.stereotype.Service;

import com.ebm.estoque.domain.Movimentacao;
import com.ebm.exceptions.DataIntegrityException;


@Service
public interface MovimentacaoService {
	public static final String DATAINTEGRITY_NOPRODUTOS = DataIntegrityException.DEFAULT + ": Não há produtos.";
	public static final String DATAINTEGRITY_NOTIPO =  DataIntegrityException.DEFAULT + ": Um movimentacao tem que ser de entrada ou saída";
	public static final String DATAINTEGRITY_SAIDAWITHFORNECEDOR =  DataIntegrityException.DEFAULT + ": Um movimentacao de saída nao deve possuir fornecedores";
	Movimentacao save(Movimentacao movimentacao);

}
