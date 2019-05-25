package com.ebm.security.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ebm.exceptions.ObjectNotFoundException;
import com.ebm.security.Grupo;
import com.ebm.security.Usuario;
import com.ebm.security.dto.GrupoDTO;
import com.ebm.security.repository.GrupoRepository;

@Service
public class GrupoService {
	@Autowired
	private GrupoRepository grupoRepository;
	@Autowired
	private UsuarioService userService;

	public Grupo find(Integer id) {
		Optional<Grupo> obj = grupoRepository.findById(id);

		return obj.orElseThrow(() -> new ObjectNotFoundException("Grupo de permissões nao encontrado! id: " + id));
	}

	@Transactional
	public Grupo insert(Grupo grupo) {

		grupo = grupoRepository.save(grupo);

		if (grupo.getUsuarios() != null || !grupo.getUsuarios().isEmpty()) {
			grupo.setUsuarios(userService
					.findAllById(grupo.getUsuarios().stream().map(u -> u.getId()).collect(Collectors.toList())));

			// percorrendo cada usuario e setando o grupo criado
			for (Usuario user : grupo.getUsuarios()) {
				user.setGrupo(grupo);
			}
			grupo.setUsuarios(userService.saveAll(grupo.getUsuarios()));
		}

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

	public Page<GrupoDTO> findBy(String nome, PageRequest pageRequest) {
		Set<Integer> ids = new HashSet<>(grupoRepository.findIdAll());

		if (nome != null && !nome.equals(""))
			ids.retainAll(grupoRepository.findAllIdByNome(nome));

		List<GrupoDTO> grupos = ids.stream().map(id -> new GrupoDTO(find(id))).collect(Collectors.toList());

		return new PageImpl<>(grupos, pageRequest, grupos.size());
	}

}
