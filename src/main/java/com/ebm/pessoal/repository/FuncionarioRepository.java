package com.ebm.pessoal.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.pessoal.domain.Cargo;
import com.ebm.pessoal.domain.Funcionario;
import com.ebm.pessoal.domain.Pessoa;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer> {

	@Transactional(readOnly=true)
	Optional<Funcionario> findOneByMatricula(String matricula);

	@Transactional(readOnly=true)
	Page<Funcionario> findAllByPessoa(Example<Pessoa> pessoa, Pageable page);

	@Transactional(readOnly=true)
	Page<Funcionario> findAllByCargo(Cargo cargo, Pageable page);
	
	@Transactional(readOnly=true)
	Optional<Funcionario> findOneByPessoa(Pessoa pessoa);
	
	@Transactional(readOnly=true)
	Page<Funcionario> findAll(Pageable page);
	
	@Transactional(readOnly=true)
	int countByCargo(Cargo cargo);
	
	@Transactional(readOnly=true)
	@Query("Select f FROM Funcionario f WHERE f.pessoa.id = ?1")
	Optional<Funcionario> findByPessoaId(Integer id);
	
	@Transactional(readOnly=true)
	@Query("Select f FROM Funcionario f WHERE LOWER(f.cargo.nomeCargo) LIKE LOWER(?1)")
	Collection<? extends Funcionario> findByCargoName(String cargo);
	
	@Query("Select f.id FROM Funcionario f WHERE LOWER(f.pessoa.nome) LIKE LOWER (?1) ")
	List<Integer> findAllIdByNomeLike(String nome);
	
	@Query("Select f.id FROM Funcionario f join f.pessoa.email e WHERE LOWER(e) LIKE LOWER (?1)  AND e.principal = true ")
	List<Integer> findAllIdByEmailPrincipalLike(String email);

}
