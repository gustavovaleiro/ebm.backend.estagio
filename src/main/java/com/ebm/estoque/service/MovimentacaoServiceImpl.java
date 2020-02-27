package com.ebm.estoque.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.estoque.domain.Movimentacao;
import com.ebm.estoque.domain.Produto;
import com.ebm.estoque.domain.ProdutoMovimentacao;
import com.ebm.estoque.domain.enums.TipoMovimentacao;
import com.ebm.estoque.dtos.MovimentacaoListDTO;
import com.ebm.estoque.repository.MovimentacaoRepository;
import com.ebm.estoque.repository.ProdutoMovimentacaoRepository;
import com.ebm.estoque.service.interfaces.ItemService;
import com.ebm.estoque.service.interfaces.MovimentacaoService;
import com.ebm.geral.exceptions.DataIntegrityException;
import com.ebm.geral.service.AbstractRestService;
import com.ebm.geral.utils.Utils;
import com.ebm.pessoal.domain.Fornecedor;
import com.ebm.pessoal.service.interfaces.FornecedorService;

@Service
public class MovimentacaoServiceImpl extends AbstractRestService<Integer, Movimentacao> implements MovimentacaoService {

	@Autowired
	private MovimentacaoRepository movimentacaoRepository;
	@Autowired
	private ProdutoMovimentacaoRepository pMovimentacaoRepository;

	@Autowired
	private FornecedorService fornecedorService;
	@Autowired
	private ItemService itemService;

	

	private void recuperaProdutosFrom(Movimentacao movimentacao) {
		movimentacao.getProdutoMovimentacao().stream().forEach(pM ->{
			pM.setProduto((Produto) itemService.findById(pM.getProduto().getId()));
		});
		
	}

	private int valueOrZero(Integer value) {
		return Optional.ofNullable(value).orElse(0);
	}

	private void recuperaFornecedorFrom(Movimentacao movimentacao) {
		if (movimentacao.getTipoMovimentacao() == TipoMovimentacao.ENTRADA && movimentacao.getFornecedores() != null
				&& movimentacao.getFornecedores().size() > 0)
			movimentacao.setFornecedores(movimentacao.getFornecedores().stream()
					.map(f -> fornecedorService.findById(f.getId())).collect(Collectors.toSet()));
	}

	private void garanteIntegridade(Movimentacao movimentacao) {
		if (movimentacao.getProdutoMovimentacao() == null || movimentacao.getProdutoMovimentacao().size() == 0)
			throw new DataIntegrityException(DATAINTEGRITY_NOPRODUTOS);
		if (movimentacao.getTipoMovimentacao() == null)
			throw new DataIntegrityException(DATAINTEGRITY_NOTIPO);
		if (movimentacao.getTipoMovimentacao() == TipoMovimentacao.SAIDA
				&& (movimentacao.getFornecedores() != null && movimentacao.getFornecedores().size() > 0))
			throw new DataIntegrityException(DATAINTEGRITY_SAIDAWITHFORNECEDOR);
	}

	@Override
	public List<Movimentacao> saveAll(List<Movimentacao> movimentacoes) {
		return movimentacoes.stream().map(m -> this.save(m)).collect(Collectors.toList());
	}

	
	@Transactional
	@Override
	public Page<MovimentacaoListDTO> findBy(TipoMovimentacao tipo, String documento, List<Integer> fornecedoresId,
			List<Integer> produtosId, PageRequest page) {
		ExampleMatcher matcher = Utils.getExampleMatcherForDinamicFilter(true);

		Movimentacao mov = new Movimentacao();
		mov.setTipoMovimentacao(tipo);
		mov.setDocumento(documento);
		List<Movimentacao> movs = new ArrayList<>(movimentacaoRepository.findAll(Example.of(mov, matcher), page).getContent());

		if (!nullOrEmpty(fornecedoresId)) {
			List<Fornecedor> fornecedores = fornecedoresId.stream().map(f -> Fornecedor.ofId(f)).collect(Collectors.toList());
			retainOrAddIfNull(movs, movimentacaoRepository.findDistinctByFornecedoresIn(fornecedores));
		}
		if (!nullOrEmpty(produtosId)) {
			List<Produto> produtos = produtosId.stream().map(p -> Produto.ofId(p)).collect(Collectors.toList());
			retainOrAddIfNull(movs, movimentacaoRepository.findDistinctByProdutosIn(produtos));
		}

		return new PageImpl<>(movs.stream().map(m -> new MovimentacaoListDTO(m)).collect(Collectors.toList()), page,
				movs.size());

	}

	private boolean nullOrEmpty(List<?> obj) {
		return obj == null || obj.isEmpty();

	}

	private void retainOrAddIfNull(List<Movimentacao> base, List<Movimentacao> toRetain) {
		if (!nullOrEmpty(base))
			base.retainAll(toRetain);
		else if (!Optional.ofNullable(base).isPresent()) {
			base = new ArrayList<>();
			base.addAll(toRetain);
		} else
			base.addAll(toRetain);
	}

	@Override
	public boolean validateEntityForSave(Movimentacao movimentacao) {
		garanteIntegridade(movimentacao);
		
	//	recuperaFornecedorFrom(movimentacao);
	//	recuperaProdutosFrom(movimentacao);
		
		if(movimentacao.getId()!=null && movimentacao.getProdutoMovimentacao().stream().anyMatch(p -> p.getMovimentacao()==null) ){
			Iterator<ProdutoMovimentacao> iterator = movimentacao.getProdutoMovimentacao().stream().iterator();
			while(iterator.hasNext()) {
				ProdutoMovimentacao pm = iterator.next();
				pm.setMovimentacao(movimentacao);
			}
		}
		movimentacao = movimentacaoRepository.save(movimentacao);

		if (!Optional.ofNullable(movimentacao.getDataMovimentacao()).isPresent())
			movimentacao.setDataMovimentacao(LocalDateTime.now());
		for (ProdutoMovimentacao pM : movimentacao.getProdutoMovimentacao()) {
			pM.setMovimentacao(movimentacao);
			

			if (movimentacao.getTipoMovimentacao() == TipoMovimentacao.ENTRADA) {
				// Aqui algo pra retornar uma mensagem de alerta caso extrapole a quantidade
				// maxima de estoque
				pM.getProduto().setEstoqueAtual(
						valueOrZero(pM.getProduto().getEstoqueAtual()) + valueOrZero(pM.getQuantidade()));
			} else {
				// Aqui algo pra retornar uma mensagem de alerta caso extrapole a quantidade
				// minima de estoque
				pM.getProduto().setEstoqueAtual(
						valueOrZero(pM.getProduto().getEstoqueAtual()) - valueOrZero(pM.getQuantidade()));
			}

			pM.setProduto((Produto) itemService.save(pM.getProduto()));

		}

		
		movimentacao.setProdutoMovimentacao(new HashSet<ProdutoMovimentacao>(
				pMovimentacaoRepository.saveAll(movimentacao.getProdutoMovimentacao())));
		return true;
	}

	@Override
	public JpaRepository<Movimentacao, Integer> getRepository() {
			return this.movimentacaoRepository;
	}
}
