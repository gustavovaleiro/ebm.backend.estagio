package com.ebm.estoque.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.estoque.domain.CategoriaItem;

@Repository
public interface CategoriaItemRepository extends JpaRepository<CategoriaItem, Integer> {
	@Transactional(readOnly = true)
	Optional <CategoriaItem> findByNomeIgnoreCaseLike(String nome);
	@Transactional(readOnly = true)
	Page<CategoriaItem> findByNomeIgnoreCaseLike(String nome, Pageable page);
}
