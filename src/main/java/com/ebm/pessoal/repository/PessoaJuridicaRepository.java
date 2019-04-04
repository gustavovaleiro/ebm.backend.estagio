package com.ebm.pessoal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.pessoal.domain.PessoaJuridica;
@Repository
public interface PessoaJuridicaRepository extends JpaRepository<PessoaJuridica, Integer> {
	
	
	@Transactional(readOnly=true)
	Optional<PessoaJuridica> findOneByCnpj(String CNPJ);

	@Transactional(readOnly=true)
	List<PessoaJuridica> findAllByInscricaoEstadualIgnoreCaseContaining(String inscricaoEstadual);

	@Transactional(readOnly=true)
	@Query("SELECT pj FROM PessoaJuridica pj INNER JOIN pj.email email WHERE email LIKE ?1")
	List<PessoaJuridica> findAllByEmailLike(String email);
	
	@Transactional(readOnly=true)
	List<PessoaJuridica> findAllByRazaoSocialIgnoreCaseContaining(String nome);
	
	@Transactional(readOnly=true)
	List<PessoaJuridica> findAllByNomeLikeIgnoreCase(String nome);
	
	@Transactional(readOnly=true)
	@Query("SELECT pj.id FROM PessoaJuridica pj")
	List<Integer> findIdOfAll();
	
	@Transactional(readOnly=true)
	@Query("SELECT pj.id FROM PessoaJuridica pj WHERE LOWER(pj.nome) LIKE LOWER(?1)")
	List<Integer> findIdOfAllWithNameContain(String nome);
	
	@Transactional(readOnly=true)
	@Query("SELECT pj.id FROM PessoaJuridica pj WHERE LOWER(pj.razaoSocial) LIKE LOWER(?1)")
	List<Integer> findIdOfAllWithRazaoSocialContain(String razaoSocial);
}
