package com.ebm.auth.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ebm.auth.Permissao;

public interface PermissaoRepository extends JpaRepository<Permissao, Integer> {
	Page<Permissao> findByDescLikeIgnoreCase(String desc, Pageable page);
}
