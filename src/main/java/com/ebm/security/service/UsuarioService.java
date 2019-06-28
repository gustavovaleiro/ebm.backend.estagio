package com.ebm.security.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.geral.exceptions.DataIntegrityException;
import com.ebm.geral.exceptions.ObjectNotFoundException;
import com.ebm.geral.utils.Utils;
import com.ebm.pessoal.domain.Funcionario;
import com.ebm.pessoal.service.FuncionarioService;
import com.ebm.security.Usuario;
import com.ebm.security.dto.UsuarioListDTO;
import com.ebm.security.dto.UsuarioNewDTO;
import com.ebm.security.dto.UsuarioUpdateDTO;
import com.ebm.security.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {
	public static final String DATAINTEGRITY = "Um usuario precisa de um grupo de permissões";
	public static final String DATAINTEGRITY_FUNCASSO = "Um usuario precisa de um funcionario associado";
	public static final String DATAINTEGRITY_CHANGEFUNC = "Você nao pode trocar o funcionario de um usuario";
	public static final String ONFE_BYUSERNAME = ObjectNotFoundException.DEFAULT + " um usuario com o login passado.";
	public static final String DATAINTEGRITY_DUPLICATEFUNC = "Já existe um usuário para o funcionario passado";

	@Autowired
	private UsuarioRepository userRepository;
	@Autowired
	private FuncionarioService funcionarioService;
	@Autowired
	private BCryptPasswordEncoder pEncoder;
	public UsuarioService() {
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			return new Usuario(this.findByUserName(username));
		} catch (ObjectNotFoundException ex) {
			throw new UsernameNotFoundException(ex.getMessage() + username);
		} 
		
	}
	
	private Usuario findByUserName(String username) {
		Optional<Usuario> user = userRepository.findByLogin(username);
		return user.orElseThrow( () -> new ObjectNotFoundException(ONFE_BYUSERNAME));
	}

	// INSERT
	@Transactional
	public Usuario save(UsuarioNewDTO userDTO) {
		Usuario user = fromDTO(userDTO);
		user.setSenha(pEncoder.encode(user.getSenha()));
		Utils.audita(user.getHistorico());
		return userRepository.save(user);
	}
	 
	private void findFuncionario(Usuario user, Integer id) {
		if(id == null)
			throw new DataIntegrityException(DATAINTEGRITY_FUNCASSO);
		if(userRepository.findById(id).isPresent()) {
			Usuario user_ = userRepository.findById(id).get();
			if(user.getId() == null || user_.getId() != user.getId())
			throw new DataIntegrityException(DATAINTEGRITY_DUPLICATEFUNC);
		}
		
		user.setFuncionario(funcionarioService.findById(id));
	}


	public Usuario update(UsuarioUpdateDTO userUp) {
		Usuario old = fromDTO(userUp);
		
		Utils.audita(old.getHistorico());
		
		return this.userRepository.save(old);
	}

	public Usuario fromDTO(UsuarioUpdateDTO userUp) {
		Usuario old = this.findById(userUp.getId());
		
		if(userUp.getLogin()!= null && !userUp.getLogin().equals(old.getLogin()))
			old.setLogin(userUp.getLogin());
		
		if(userUp.getPermissoes() != null)		
			old.setPermissoes(userUp.getPermissoes());
		return old;
	}
	
	public Usuario fromDTO(UsuarioNewDTO userDTO) {
		Usuario usuario = new Usuario(null, userDTO.getLogin(), userDTO.getSenha(), userDTO.getPermissoes());
		findFuncionario(usuario,userDTO.getFuncionario_id());
		return usuario;
	}
	public Usuario findById(Integer id) {
		if(!Optional.ofNullable(id).isPresent())
			throw new DataIntegrityException("Id não pode ser nulo");
		
		return this.userRepository.findById(id).orElseThrow( () -> new ObjectNotFoundException("Não foi possivel encontrar um usuario com o id fornecido"));
	}

	

	public List<Usuario> saveAll(List<Usuario> usuarios) {
		return usuarios.stream().map(u -> UsuarioNewDTO.from(u)).map( u -> this.save(u)).collect(Collectors.toList());
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

	//metodo nenhum pouco perfomatico
	public Page<UsuarioListDTO> findBy(String nome , String login, String email,
			PageRequest pageRequest) {
		Set<Integer> ids = new HashSet<>();

		ids = userRepository.findAllId();
		if (nome != null)
			ids.retainAll(funcionarioService.findIdByNomeLike(nome));

		if (login != null)
			ids.retainAll(userRepository.findAllIdByLogin("%"+login+"%"));

		if (email != null)
			ids.retainAll(funcionarioService.findIdByEmailPrincipalLike("%"+email+"%"));

		List<UsuarioListDTO> usuarios = userRepository.findAllById(ids).stream().map(u -> new UsuarioListDTO(u))
				.collect(Collectors.toList());

		return new PageImpl<>(usuarios, pageRequest, usuarios.size());
	}



	

}
