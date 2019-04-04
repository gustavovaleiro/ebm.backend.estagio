package com.ebm.auth.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.auth.Modulo;
import com.ebm.auth.Permissao;

public interface PermissaoRepository extends JpaRepository<Permissao, Integer> {
	
	@Transactional(readOnly=true)
	Page<Permissao> findByDescLikeIgnoreCase(String desc, Pageable page);
	
	@Transactional(readOnly=true)
	Page<Permissao> findAllByModulo(Modulo modulo, Pageable pageRequest);
}
