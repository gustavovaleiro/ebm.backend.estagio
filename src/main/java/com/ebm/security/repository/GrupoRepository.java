package com.ebm.security.repository;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.security.Grupo;
import com.ebm.security.dto.GrupoDTO;

public interface GrupoRepository extends JpaRepository<Grupo, Integer> {
	@Transactional(readOnly = true)
	Page<Grupo> findByNomeLikeIgnoreCase(String nome, Pageable page);

	@Transactional(readOnly = true)
	@Query("SELECT new com.ebm.auth.dto.GrupoDTO(g.id, g.nome) FROM Grupo g")
	Page<GrupoDTO> findAllResumido(Pageable page);
	
	@Transactional(readOnly = true)
	@Query("SELECT g.id  FROM Grupo g")
	Collection<Integer> findIdAll();
	
	@Transactional(readOnly = true)
	@Query("SELECT g.id  FROM Grupo g WHERE LOWER(g.nome) LIKE LOWER(?1)")
	Collection<Integer> findAllIdByNome(String nome);
}