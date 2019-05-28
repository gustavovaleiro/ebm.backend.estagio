package com.ebm.estoque.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.estoque.domain.Movimentacao;
import com.ebm.estoque.domain.Produto;
import com.ebm.pessoal.domain.Fornecedor;
@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Integer> {

	@Transactional(readOnly=true)
	List<Movimentacao> findDistinctByFornecedoresIn(List<Fornecedor> fornecedores);
	
	@Transactional(readOnly=true)
	@Query("SELECT distinct m FROM Movimentacao m  JOIN m.produtoMovimentacao pM WHERE pM.id.produto IN (:produtos)")
	List<Movimentacao> findDistinctByProdutosIn(List<Produto> produtos );

}
