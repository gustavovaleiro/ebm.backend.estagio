package com.ebm.pessoal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.pessoal.domain.PessoaJuridica;
@Repository
public interface PessoaJuridicaRepository extends JpaRepository<PessoaJuridica, Integer> {
	
	@Transactional(readOnly=true)
	Page<PessoaJuridica> findAllByNomeLikeIgnoreCase(String nome, Pageable page);

	@Transactional(readOnly=true)
	Optional<PessoaJuridica> findOneByCnpj(String CNPJ);

	@Transactional(readOnly=true)
	Page<PessoaJuridica> findAllByRazaoSocialIgnoreCaseContaining(String CNPJ, Pageable page);

	@Transactional(readOnly=true)
	Page<PessoaJuridica> findAllByInscricaoEstadualIgnoreCaseContaining(String inscricaoEstadual, Pageable page);

	@Transactional(readOnly=true)
	@Query("SELECT pj FROM PessoaJuridica pj INNER JOIN pj.email email WHERE email LIKE ?1")
	Page<PessoaJuridica> findAllByEmailLike(String email, Pageable page);
	
	@Transactional(readOnly=true)
	List<PessoaJuridica> findAllByRazaoSocialIgnoreCaseContaining(String nome);
	
	@Transactional(readOnly=true)
	List<PessoaJuridica> findAllByNomeLikeIgnoreCase(String nome);

}
