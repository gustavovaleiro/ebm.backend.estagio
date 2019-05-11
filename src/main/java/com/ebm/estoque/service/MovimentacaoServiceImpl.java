package com.ebm.estoque.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.estoque.domain.Movimentacao;
import com.ebm.estoque.domain.Produto;
import com.ebm.estoque.domain.ProdutoMovimentacao;
import com.ebm.estoque.domain.enums.TipoMovimentacao;
import com.ebm.estoque.domain.interfaces.OrigemMovimentacao;
import com.ebm.estoque.repository.MovimentacaoRepository;
import com.ebm.estoque.repository.ProdutoMovimentacaoRepository;
import com.ebm.estoque.service.interfaces.FornecedorService;
import com.ebm.estoque.service.interfaces.ItemService;
import com.ebm.estoque.service.interfaces.MovimentacaoService;
import com.ebm.exceptions.DataIntegrityException;

@Service
public class MovimentacaoServiceImpl implements MovimentacaoService {

	
	@Autowired
	private MovimentacaoRepository movimentacaoRepository;
	@Autowired
	private ProdutoMovimentacaoRepository pMovimentacaoRepository;

	@Autowired
	private FornecedorService fornecedorService;
	@Autowired
	private ItemService itemService;
	
	public void baixaEstoque(OrigemMovimentacao origem ) {}
	public void cancelarBaixaEstoque(OrigemMovimentacao origem) {}


	@Transactional
	@Override
	public Movimentacao save(Movimentacao movimentacao) {
		garanteIntegridade(movimentacao);
		
		if(movimentacao.getId() == null)
			movimentacao.setDataCadastro(LocalDateTime.now());
			
		movimentacao = movimentacaoRepository.save(movimentacao);
			
		
		for(ProdutoMovimentacao pM : movimentacao.getProdutosMovimentacao()){
			pM.setMovimentacao(movimentacao);
			pM.setProduto((Produto) itemService.findById(pM.getProduto().getId()));
			if(movimentacao.getTipoMovimentacao() == TipoMovimentacao.ENTRADA) {
				
				//Aqui algo pra retornar uma mensagem de alerta caso extrapole a quantidade maxima de estoque
				pM.getProduto().setEstoqueAtual( pM.getProduto().getEstoqueAtual() + pM.getQuantidade() );
				
			} else {
				
				//Aqui algo pra retornar uma mensagem de alerta caso extrapole a quantidade minima de estoque
				pM.getProduto().setEstoqueAtual( pM.getProduto().getEstoqueAtual() - pM.getQuantidade() );
			}
			
			pM.setProduto((Produto) itemService.save(pM.getProduto()));
			
			
		}
		
		recuperaFornecedorFrom(movimentacao);
		movimentacao.setProdutosMovimentacao( new HashSet<ProdutoMovimentacao>(pMovimentacaoRepository.saveAll(movimentacao.getProdutosMovimentacao())));
		
		return movimentacao;
	}
	private void recuperaFornecedorFrom(Movimentacao movimentacao) {
		if(movimentacao.getTipoMovimentacao() == TipoMovimentacao.ENTRADA && movimentacao.getFornecedores() != null && movimentacao.getFornecedores().size() > 0) 
			movimentacao.setFornecedores( movimentacao.getFornecedores().stream().map(f -> fornecedorService.findById(f.getId())).collect(Collectors.toSet()));
	}


	private void garanteIntegridade(Movimentacao movimentacao) {
		if(movimentacao.getProdutosMovimentacao() == null || movimentacao.getProdutosMovimentacao().size() == 0)
			throw new DataIntegrityException(DATAINTEGRITY_NOPRODUTOS);
		if(movimentacao.getTipoMovimentacao() == null)
			throw new DataIntegrityException(DATAINTEGRITY_NOTIPO);
		if(movimentacao.getTipoMovimentacao() == TipoMovimentacao.SAIDA &&  (movimentacao.getFornecedores() != null && movimentacao.getFornecedores().size() > 0 )  )
			throw new DataIntegrityException(DATAINTEGRITY_SAIDAWITHFORNECEDOR);
	}
}
