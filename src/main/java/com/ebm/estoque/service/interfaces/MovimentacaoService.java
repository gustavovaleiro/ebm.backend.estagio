package com.ebm.estoque.service.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ebm.estoque.domain.Movimentacao;
import com.ebm.estoque.domain.enums.TipoMovimentacao;
import com.ebm.estoque.dtos.MovimentacaoListDTO;
import com.ebm.geral.exceptions.DataIntegrityException;
import com.ebm.geral.exceptions.ObjectNotFoundException;


@Service
public interface MovimentacaoService {
	public static final String DATAINTEGRITY_NOPRODUTOS = DataIntegrityException.DEFAULT + ": Não há produtos.";
	public static final String DATAINTEGRITY_NOTIPO =  DataIntegrityException.DEFAULT + ": Um movimentacao tem que ser de entrada ou saída";
	public static final String DATAINTEGRITY_SAIDAWITHFORNECEDOR =  DataIntegrityException.DEFAULT + ": Um movimentacao de saída nao deve possuir fornecedores";
	public static final String DATAINTEGRITY_IDNULL = DataIntegrityException.DEFAULT + ": O id não pode ser nulo";
	public static final String ONFE_NOTFOUNDBYID =ObjectNotFoundException.DEFAULT + " uma movimentacaçõ com o id: ";
	Movimentacao save(Movimentacao movimentacao);
	List<Movimentacao> saveAll(List<Movimentacao> asList);
	Movimentacao findById(Integer id);
	Page<MovimentacaoListDTO> findBy(TipoMovimentacao tipo, String documento, List<Integer> fornecedores,
			List<Integer> produtos, PageRequest page);
	void deleteById(Integer id);

}
