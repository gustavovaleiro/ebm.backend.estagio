package com.ebm.estoque.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebm.estoque.domain.Unidade;

@Repository
public interface UnidadeRepository extends JpaRepository<Unidade, Integer> {

	Optional<Unidade> findByAbrev(String abrev);

}
