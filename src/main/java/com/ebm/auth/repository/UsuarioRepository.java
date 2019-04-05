package com.ebm.auth.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.auth.Grupo;
import com.ebm.auth.Usuario;
import com.ebm.pessoal.domain.Funcionario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	
	@Transactional(readOnly = true)
	Optional<Usuario> findOneByFuncionario(Funcionario funcionario);
	
	@Transactional(readOnly = true)
	Page<Usuario> findByGrupo(Grupo grupo, Pageable pageRequest);
	
	@Transactional(readOnly = true)
	Optional<Usuario> findOneByLogin(String login);
	
	@Transactional(readOnly = true)
	@Query("SELECT u.id FROM Usuario u WHERE u.grupo.id = ?1")
	Set<Integer> findAllIdByGrupo(Integer grupo_id);
	
	@Transactional(readOnly = true)
	@Query("SELECT u.id FROM Usuario u WHERE u.funcionario.id in ?1")
	Set<Integer> findAllIdOfFuncionarios(Set<Integer> findAllIdByNome);

	@Transactional(readOnly = true)
	@Query("SELECT u.id FROM Usuario u WHERE LOWER(u.login) LIKE LOWER(?1)")
	Set<Integer> findAllIdByLogin(String login);

	@Transactional(readOnly = true)
	@Query("SELECT u.id FROM Usuario u WHERE LOWER(u.emailRecovery) LIKE LOWER(?1)")
	Set<Integer> findAllIdByEmail(String email);
	
	@Transactional(readOnly = true)
	@Query("SELECT u.id FROM Usuario u")
	Set<Integer> findAllId();
	


}
