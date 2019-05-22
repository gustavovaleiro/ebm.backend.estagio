package com.ebm.auth.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.auth.Grupo;
import com.ebm.auth.Usuario;
import com.ebm.auth.dto.UsuarioListDTO;
import com.ebm.auth.repository.UsuarioRepository;
import com.ebm.exceptions.DataIntegrityException;
import com.ebm.exceptions.ObjectNotFoundException;
import com.ebm.pessoal.domain.Funcionario;
import com.ebm.pessoal.service.EmailService;
import com.ebm.pessoal.service.FuncionarioService;

@Service
public class UsuarioService {// implements UserDetailsService {
	@Autowired
	private UsuarioRepository userRepository;
	@Autowired
	private FuncionarioService funcionarioService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private GrupoService grupoService;
	@Autowired
	private BCryptPasswordEncoder pEncoder;
	public UsuarioService() {
	}

	// method userdetailsservice
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		Optional<Usuario> user = userRepository.findOneByLogin(username);
//		return new UserSS(user.orElseThrow(() -> new UsernameNotFoundException(username)));
//	}
//
//	public static UserSS authenticated() {
//		try {
//			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		} catch (Exception e) {
//			return null;
//		}
//	}
	/////
	/// crud do usuario

	
	// INSERT
	@Transactional
	public Usuario save(Usuario user) {
		garantirIntegridade(user);
		
		user.setSenha(pEncoder.encode(user.getSenha()));
		return userRepository.save(user);
	}

	private void garantirIntegridade(Usuario user) {
		if(user.getGrupo()==null)
			throw new DataIntegrityException("Um usuario precisa de um grupo de permissões");
		else
			user.setGrupo(grupoService.find(user.getGrupo().getId()));
		
		if(user.getFuncionario().getId() == null)
			throw new DataIntegrityException("Um usuario precisa de um funcionario associado");
		else {
			if(user.getId() != null) {
				Usuario userR = userRepository.findById(user.getId()).get();
				if(!userR.getFuncionario().equals(user.getFuncionario())) {
					throw new DataIntegrityException("Você nao pode trocar o funcionario de um usuario");
				}
			}	
			user.setFuncionario(funcionarioService.findById(user.getFuncionario().getId()));
		}
	}


	public List<Usuario> saveAll(List<Usuario> usuarios) {
		return usuarios.stream().map(f -> this.save(f)).collect(Collectors.toList());
	}

	// DELETE

	public void deleteById(Integer id) {
		find(id);
		userRepository.deleteById(id);
	}

	// FIND
	public Usuario find(Integer id) {
		Optional<Usuario> obj = userRepository.findById(id);

		return obj.orElseThrow(() -> new ObjectNotFoundException("Usuario nao encontrado! id: " + id));
	}

	public Usuario findByFuncionario(Funcionario funcionario) {
		Optional<Usuario> usuario = userRepository.findOneByFuncionario(funcionario);
		return usuario.orElseThrow(() -> new ObjectNotFoundException(
				"Não foi possivel encontrar o usuario correspondente ao: " + funcionario.getPessoa().getNome()));
	}

	public List<Usuario> findAll() {
		return userRepository.findAll();
	}


	public Usuario findByCpfOrCnpj(String document) {
		return findByFuncionario(funcionarioService.findByCpfOrCnpj(document));
	}

	public List<Usuario> findAllById(List<Integer> ids) {
		
		return userRepository.findAllById(ids);
	}

	public Page<UsuarioListDTO> findBy(String nome, Integer grupo_id, String login, String email,
			PageRequest pageRequest) {
		Set<Integer> ids = new HashSet<>();

		ids = userRepository.findAllId();
		if (nome != null)
			ids.retainAll(userRepository.findAllIdOfFuncionarios(funcionarioService.findAllIdByNome(nome)));

		if (grupo_id != null)
			ids.retainAll(userRepository.findAllIdByGrupo(grupo_id));

		if (login != null)
			ids.retainAll(userRepository.findAllIdByLogin(login));

		if (email != null)
			ids.retainAll(userRepository.findAllIdByEmail(email));

		List<UsuarioListDTO> usuarios = userRepository.findAllById(ids).stream().map(u -> new UsuarioListDTO(u))
				.collect(Collectors.toList());

		return new PageImpl<>(usuarios, pageRequest, usuarios.size());
	}

}
