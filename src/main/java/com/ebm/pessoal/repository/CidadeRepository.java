package com.ebm.pessoal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.pessoal.domain.Cidade;
import com.ebm.pessoal.domain.Estado;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Integer> {

	List<Cidade> findAllByEstado(Estado estado);

	Optional<Cidade> findOneByNome(String nome);
	
	
	@Transactional(readOnly=true)
	@Query("SELECT c FROM Cidade c WHERE LOWER(c.nome) LIKE LOWER(?1) AND LOWER(c.estado.uf) = LOWER(?2)")
	Optional<Cidade>  findOneByNomeAndEstado(String nome, String uf);
	@Transactional(readOnly=true)
	boolean existsByEstado(Estado estado);

}
