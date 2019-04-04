package com.ebm.pessoal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.pessoal.domain.PessoaFisica;
import com.ebm.pessoal.domain.RG;

@Repository
public interface PessoaFisicaRepository extends JpaRepository<PessoaFisica, Integer> {
	
	@Transactional(readOnly=true)
	Page<PessoaFisica> findAllByNomeLikeIgnoreCase(String nome, Pageable page);
	
	@Transactional(readOnly=true)
	Optional<PessoaFisica> findOneByCpf(String CPF);
	
	@Transactional(readOnly=true)
	@Query("SELECT pf FROM PessoaFisica pf INNER JOIN pf.email email WHERE LOWER(email) LIKE LOWER(?1)")
	Page<PessoaFisica> findAllByEmailLike(String email, Pageable page);
	
	@Transactional(readOnly=true)
	Page<PessoaFisica> findAllByRG(Example<RG> example, Pageable page);
	
	@Transactional(readOnly=true)
	List<PessoaFisica> findAllByNomeLikeIgnoreCase(String nome);
}
