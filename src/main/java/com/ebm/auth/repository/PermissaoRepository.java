package com.ebm.auth.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.auth.Permissao;

public interface PermissaoRepository extends JpaRepository<Permissao, Integer> {
	
	@Transactional(readOnly=true)
	Page<Permissao> findByDescLikeIgnoreCase(String desc, Pageable page);
	
	@Transactional(readOnly=true)
	@Query(value = "SELECT p.ID  FROM PERMISSAO p WHERE p.MODULO = ?1", nativeQuery = true)
	List<Permissao> findAllByModulo(Integer modulo);

	@Transactional(readOnly = true)
	@Query("SELECT p.id  FROM Permissao p")
	Collection<Integer> findIdOfAll();
}
