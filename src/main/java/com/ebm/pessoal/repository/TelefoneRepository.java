package com.ebm.pessoal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.pessoal.domain.Telefone;
@Repository
public interface TelefoneRepository extends JpaRepository<Telefone, Integer> {
	
	@Transactional(readOnly=true)
	@Query(value = "SELECT * FROM TELEFONE WHERE PESSOA_ID = ?1 ", nativeQuery = true)
	List<Telefone> findByPessoa(Integer id);
	
	@Transactional(readOnly=true)
	Long countByDDDAndNumero(String ddd, String telefone);
	
	@Transactional(readOnly=true)
	@Query(value = "SELECT distinct TIPO FROM TELEFONE GROUP BY TIPO", nativeQuery = true)
	List<String> findAllTipoTelefone();
	
}
