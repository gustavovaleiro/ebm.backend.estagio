package com.ebm.pessoal.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ebm.pessoal.domain.PessoaFisica;

@Repository
public interface PessoaFisicaRepository extends JpaRepository<PessoaFisica, Integer> {
	
	
	Page<PessoaFisica> findAllByNomeLikeIgnoreCase(String desc, Pageable page);
	
	Optional<PessoaFisica> findOneByCpf(String CPF);

	@Query("SELECT pf FROM PessoaFisica pf INNER JOIN pf.email email WHERE email LIKE ?1")
	Page<PessoaFisica> findAllByEmailLike(String email, Pageable page);
	
	@Query("SELECT pf FROM PessoaFisica pf  INNER JOIN pf.RG  rg WHERE rg.RG = ?1")
	Optional<PessoaFisica> findOneByRGNumero(String rg);
	@Query("SELECT pf FROM PessoaFisica pf INNER JOIN pf.RG  rg  WHERE rg.emissor LIKE ?1")
	Page<PessoaFisica> findAllByRGEmissor( String emissor, Pageable page);
	@Query("SELECT pf FROM PessoaFisica pf INNER JOIN pf.RG  rg WHERE rg.UF LIKE ?1")
	Page<PessoaFisica> findAllByRGUF( String uf, Pageable page);
}
