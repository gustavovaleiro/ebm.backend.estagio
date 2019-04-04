package com.ebm.auth.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.ebm.auth.Grupo;
import com.ebm.auth.GrupoPermissao;
import com.ebm.auth.Usuario;
import com.ebm.auth.dto.GrupoDTO;
import com.ebm.auth.repository.GrupoRepository;
import com.ebm.exceptions.ObjectNotFoundException;

@Service
public class GrupoService {
	@Autowired
	private GrupoRepository grupoRepository;
	@Autowired 
	private UsuarioService userService;

	@Autowired
	private GrupoPermissaoService grupoPermissaoService;
	public Grupo find(Integer id) {
		Optional<Grupo> obj = grupoRepository.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException("Grupo de permissões nao encontrado! id: " + id));
	}
	
	@Transactional
	public Grupo insert(Grupo grupo) {
		grupo.setId(null);
		grupo = grupoRepository.save(grupo);
		
		//percorrendo cada usuario e setando o grupo criado
	    for(Usuario user : grupo.getUsuarios()) {
	    	user.setGrupo(grupo);
	    }
	    userService.updateAll(grupo.getUsuarios());
	    
		//percorrendo cada grupoPermissao e setando o grupo criado
		for(GrupoPermissao permissao : grupo.getPermissoes()) {
			permissao.setPermissao(grupoPermissaoService.findPermissao(permissao.getPermissao().getId()));
			permissao.setGrupo(grupo);
		}
		grupoPermissaoService.insertAll(grupo.getPermissoes());
		
		
		return grupo;
	}
	
	public Grupo update(Grupo newUser) {
		 @SuppressWarnings("unused")
		 Grupo old = this.find(newUser.getId());
		 return grupoRepository.save(newUser);
	}
	public void deleteById(Integer id) {
		find(id);
		grupoRepository.deleteById(id);
	}
	
	public Page<GrupoDTO> findAllResumo( Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return grupoRepository.findAllResumido(pageRequest);
	}
	

}
