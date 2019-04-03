package com.ebm.auth.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.ebm.auth.Grupo;
import com.ebm.auth.Usuario;
import com.ebm.auth.repository.UsuarioRepository;
import com.ebm.exceptions.ObjectNotFoundException;
import com.ebm.pessoal.domain.Funcionario;

@Service
public class UsuarioService {//implements UserDetailsService {
	@Autowired
	private UsuarioRepository userRepository;
	
	public UsuarioService() {}

	//method userdetailsservice
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
	///crud do usuario
	
	public Usuario find(Integer id) {
		Optional<Usuario> obj = userRepository.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException("Usuario nao encontrado! id: " + id));
	}
	
	public Usuario insert(Usuario user) {
		user.setId(null);
		return userRepository.save(user);
	}
	
	public Usuario update(Usuario newUser) {
		 @SuppressWarnings("unused")
		 Usuario old = this.find(newUser.getId());
		 return userRepository.save(newUser);
	}
	public void deleteById(Integer id) {
		userRepository.deleteById(id);
	}
	public Usuario findByFuncionario(Funcionario funcionario) {
		Optional<Usuario> usuario = userRepository.findOneByFuncionario(funcionario);
		return usuario.orElseThrow(()-> new ObjectNotFoundException("Não foi possivel encontrar o usuario correspondente ao: " + funcionario.getPessoa().getNome()));
	}

	public Page<Usuario> findAllPage( Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return userRepository.findAll(pageRequest);
	}
	public List<Usuario> findAll(){
		return userRepository.findAll();
	}
	public Page<Usuario> findByGrupo(Grupo grupo, Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return userRepository.findByGrupo(grupo, pageRequest);
	}

	public void updateAll(List<Usuario> usuarios) {
		userRepository.saveAll(usuarios);
	}
}
