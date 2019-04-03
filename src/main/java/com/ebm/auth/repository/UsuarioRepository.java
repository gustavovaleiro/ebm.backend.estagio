package com.ebm.auth.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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

}
