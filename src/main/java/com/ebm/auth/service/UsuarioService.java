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
import org.springframework.stereotype.Service;

import com.ebm.auth.Grupo;
import com.ebm.auth.Usuario;
import com.ebm.auth.dto.UsuarioListDTO;
import com.ebm.auth.repository.UsuarioRepository;
import com.ebm.exceptions.ObjectNotFoundException;
import com.ebm.pessoal.domain.Funcionario;
import com.ebm.pessoal.service.FuncionarioService;

@Service
public class UsuarioService {// implements UserDetailsService {
	@Autowired
	private UsuarioRepository userRepository;
	@Autowired
	private FuncionarioService funcionarioService;

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
	public Usuario insert(Usuario user) {
		user.setId(null);
		Funcionario func = funcionarioService.findById(user.getFuncionario().getId());
		userRepository.save(user);
		func.setUsuario(user);
		return userRepository.save(user);
	}

	// UPDATE
	public Usuario update(Usuario newUser) {
		@SuppressWarnings("unused")
		Usuario old = this.find(newUser.getId());
		return userRepository.save(newUser);
	}

	public void updateAll(List<Usuario> usuarios) {
		userRepository.saveAll(usuarios);
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

	public Page<UsuarioListDTO> findByGrupo(Grupo grupo, Integer page, Integer linesPerPage, String orderBy,
			String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return null;// userRepository.findByGrupo(grupo, pageRequest);
	}

	public Usuario findByCpfOrCnpj(String cpf, String cnpj) {
		return findByFuncionario(funcionarioService.findByCpfOrCnpj(cpf, cnpj));
	}

	public Page<UsuarioListDTO> findBy(String nome, Integer grupo_id, String login, String email,
			PageRequest pageRequest) {
		Set<Integer> ids = new HashSet<>();

		ids = userRepository.findAllId();
		if (!nome.equals(nome))
			ids.retainAll(userRepository.findAllIdOfFuncionarios(funcionarioService.findAllIdByNome(nome)));

		if (grupo_id >= 0)
			ids.retainAll(userRepository.findAllIdByGrupo(grupo_id));

		if (!login.equals(""))
			ids.retainAll(userRepository.findAllIdByLogin(login));

		if (!email.equals(""))
			ids.retainAll(userRepository.findAllIdByEmail(email));

		List<UsuarioListDTO> usuarios = userRepository.findAllById(ids).stream().map(u -> new UsuarioListDTO(u))
				.collect(Collectors.toList());

		return new PageImpl<>(usuarios, pageRequest, usuarios.size());
	}

}
