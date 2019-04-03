package com.ebm.pessoal.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ebm.pessoal.domain.PessoaJuridica;
@Repository
public interface PessoaJuridicaRepository extends JpaRepository<PessoaJuridica, Integer> {

	Page<PessoaJuridica> findAllByNomeLikeIgnoreCase(String nome, Pageable page);

	Optional<PessoaJuridica> findOneByCnpj(String CNPJ);

	Page<PessoaJuridica> findAllByRazaoSocialIgnoreCaseContaining(String CNPJ, Pageable page);

	Page<PessoaJuridica> findAllByInscricaoMunicipalIgnoreCaseContaining(String inscricaoMunicipal, Pageable page);

	Page<PessoaJuridica> findAllByInscricaoEstadualIgnoreCaseContaining(String inscricaoEstadual, Pageable page);

	@Query("SELECT pj FROM PessoaJuridica pj INNER JOIN pj.email email WHERE email LIKE ?1")
	Page<PessoaJuridica> findAllByEmailLike(String email, Pageable page);

}
