package com.ebm.auth.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ebm.auth.Grupo;
import com.ebm.auth.dto.GrupoDTO;

public interface GrupoRepository extends JpaRepository<Grupo, Integer> {
	Page<Grupo> findByNomeLikeIgnoreCase(String nome, Pageable page);
	
	@Query("SELECT new com.ebm.auth.dto.GrupoDTO(g.id, g.nome) FROM Grupo g")
	Page<GrupoDTO> findAllResumido(Pageable page);
}