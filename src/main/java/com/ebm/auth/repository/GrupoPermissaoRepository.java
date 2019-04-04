package com.ebm.auth.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.auth.Grupo;
import com.ebm.auth.GrupoPermissao;
import com.ebm.auth.GrupoPermissaoPK;

public interface GrupoPermissaoRepository extends JpaRepository<GrupoPermissao, GrupoPermissaoPK> {
	@Transactional(readOnly=true)
	@Query("SELECT p FROM GrupoPermissao p INNER JOIN p.id  id WHERE id.grupo = ?1")
	Page<GrupoPermissao> findAllByGrupo(Grupo grupo,Pageable page);
}
