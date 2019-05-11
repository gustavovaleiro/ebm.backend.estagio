package com.ebm.estoque.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebm.estoque.domain.Movimentacao;
@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Integer> {

}
