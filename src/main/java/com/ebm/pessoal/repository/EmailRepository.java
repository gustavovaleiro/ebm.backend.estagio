package com.ebm.pessoal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.pessoal.domain.Email;
@Repository
public interface EmailRepository extends JpaRepository<Email, Integer> {
	
	@Transactional(readOnly=true)
	@Query(value = "SELECT * FROM EMAIL WHERE PESSOA_ID = ?1 ", nativeQuery = true)
	List<Email> findByPessoa(Integer id);
	
	@Transactional(readOnly=true)
	Long countByEmailIgnoreCase(String email);
	
	@Transactional(readOnly=true)
	@Query(value = "SELECT distinct TIPO FROM EMAIL GROUP BY TIPO", nativeQuery = true)
	List<String> findAllTipoEmail();

}	
