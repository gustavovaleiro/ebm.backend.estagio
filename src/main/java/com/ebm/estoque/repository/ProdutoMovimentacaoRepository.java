package com.ebm.estoque.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebm.estoque.domain.ProdutoMovimentacao;
import com.ebm.estoque.domain.ProdutoMovimentacaoPK;
@Repository
public interface ProdutoMovimentacaoRepository extends JpaRepository<ProdutoMovimentacao, ProdutoMovimentacaoPK> {

}
