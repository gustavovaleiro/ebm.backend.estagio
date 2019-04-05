package com.ebm.pessoal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.pessoal.domain.PessoaFisica;
import com.ebm.pessoal.domain.RG;


@Repository
public interface PessoaFisicaRepository extends JpaRepository<PessoaFisica, Integer>{
	
	@Transactional(readOnly=true)
	List<PessoaFisica> findAllByNomeLikeIgnoreCase(String nome );
	
	@Transactional(readOnly=true)
	Optional<PessoaFisica> findOneByCpf(String CPF);
	
	@Transactional(readOnly=true)
	@Query("SELECT pf FROM PessoaFisica pf INNER JOIN pf.email email WHERE LOWER(email) LIKE LOWER(?1)")
	List<PessoaFisica> findAllByEmailLike(String email );
	
	@Transactional(readOnly=true)
	List<PessoaFisica> findAllByRG(Example<RG> example);
	
	@Transactional(readOnly=true)
	@Query("SELECT pf.id FROM PessoaFisica pf")
	List<Integer> findIdOfAll();
	
	@Transactional(readOnly=true)
	@Query("SELECT pf.id FROM PessoaFisica pf WHERE LOWER(pf.nome) LIKE LOWER(?1)")
	List<Integer> findIdOfAllWithNameContain(String nome);


}
