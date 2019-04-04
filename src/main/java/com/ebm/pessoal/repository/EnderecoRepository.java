package com.ebm.pessoal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.pessoal.domain.Endereco;
@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {
	
	@Transactional(readOnly=true)
	@Query(value = "SELECT * FROM ENDERECO WHERE PESSOA_ID = ?1 ", nativeQuery = true)
	List<Endereco> findByPessoa(Integer id);
	
	@Transactional(readOnly=true)
	@Query(value = "SELECT distinct TIPO FROM ENDERECO GROUP BY TIPO", nativeQuery = true)
	List<String> findAllTipoEndereco();

}
