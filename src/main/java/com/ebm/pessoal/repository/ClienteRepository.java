package com.ebm.pessoal.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.pessoal.domain.Cliente;
import com.ebm.pessoal.domain.Pessoa;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

	@Transactional(readOnly = true)
	Optional<Cliente> findOneByPessoa(Pessoa pessoa);

	@Transactional(readOnly = true)
	Page<Cliente> findAll(Pageable page);

	@Transactional(readOnly = true)
	Page<Cliente> findAllByPessoa(Pessoa pessoa, Pageable page);

	@Transactional(readOnly = true)
	@Query("Select c FROM Cliente c WHERE c.pessoa.id = ?1")
	Optional<Cliente> findByPessoaId(Integer id);

}
