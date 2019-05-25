package com.ebm.security.repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.pessoal.domain.Funcionario;
import com.ebm.security.Grupo;
import com.ebm.security.Usuario;

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

	@Transactional(readOnly = true)
	Optional<Usuario> findByUserName(String username);

	@Transactional(readOnly = true)
	@Query("SELECT distinct p.id FROM Pessoa p WHERE LOWER (p.nome) LIKE LOWER(?1)")
	Set<Integer> findAllIdOfFuncionariosByNome(String nome);

}
