package com.ebm.auth.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebm.auth.GrupoPermissao;
import com.ebm.auth.Permissao;
import com.ebm.auth.repository.GrupoPermissaoRepository;
import com.ebm.auth.repository.PermissaoRepository;
import com.ebm.exceptions.ObjectNotFoundException;

@Service
public class GrupoPermissaoService {
	
	@Autowired
	private PermissaoRepository permissaoRepository;
	@Autowired
	private GrupoPermissaoRepository grupoPermissaoRepository;

	public Permissao find(Integer id) {
		Optional<Permissao> permissao = permissaoRepository.findById(id);
		return permissao
				.orElseThrow(() -> new ObjectNotFoundException("Não foi possivel encontrar a permissão de id: " + id));
	}

	public void saveAll(List<GrupoPermissao> permissoes) {
		grupoPermissaoRepository.saveAll(permissoes);
		
	}
}
