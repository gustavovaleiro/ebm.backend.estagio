package com.ebm.auth.service;

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
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.ebm.auth.Grupo;
import com.ebm.auth.GrupoPermissao;
import com.ebm.auth.GrupoPermissaoPK;
import com.ebm.auth.Permissao;
import com.ebm.auth.dto.GrupoDTO;
import com.ebm.auth.repository.GrupoPermissaoRepository;
import com.ebm.auth.repository.PermissaoRepository;
import com.ebm.exceptions.ObjectNotFoundException;

@Service
public class GrupoPermissaoService {
	
	@Autowired
	private PermissaoRepository permissaoRepository;
	@Autowired
	private GrupoPermissaoRepository grupoPermissaoRepository;

	public Permissao findPermissao(Integer id) {
		Optional<Permissao> permissao = permissaoRepository.findById(id);
		return permissao
				.orElseThrow(() -> new ObjectNotFoundException("Não foi possivel encontrar a permissão de id: " + id));
	}

	public List<GrupoPermissao> insertAll(List<GrupoPermissao> permissoes) {
		return  grupoPermissaoRepository.saveAll(permissoes);
		
		
	}
	
	public GrupoPermissao update(GrupoPermissao permissao) {
		this.find(new GrupoPermissaoPK(permissao));
		return grupoPermissaoRepository.save(permissao);
	}
	
	@Transactional
	public List<GrupoPermissao>  updateAll(List<GrupoPermissao> permissoes){
		return permissoes.stream().map(p -> this.update(p)).collect(Collectors.toList());
	}
	public void delete(GrupoPermissao permissao) {
		this.find(new GrupoPermissaoPK(permissao));
		grupoPermissaoRepository.delete(permissao);
	}
	
	public GrupoPermissao find(GrupoPermissaoPK permissaoPK) {
		Optional<GrupoPermissao> permissao = grupoPermissaoRepository.findById(permissaoPK);
		return permissao.orElseThrow(() -> new ObjectNotFoundException
				("Não foi possivel encontrar a permissao: " + permissaoPK.getPermissao().getId() + " Do grupo: " + permissaoPK.getGrupo().getId()));
	}
	public Page<Permissao> findPermissoesByDesc(String desc, Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return permissaoRepository.findByDescLikeIgnoreCase(desc, pageRequest);
	}
	public Page<Permissao> findPermissoesByModulo(Integer modulo, PageRequest page){
		Set<Integer> ids = new HashSet<>(permissaoRepository.findIdOfAll());
		
		if(modulo >= 0) 
			ids.retainAll(permissaoRepository.findAllByModulo(modulo));
		
		List<Permissao> permissoes = permissaoRepository.findAllById(ids);
		
		return new PageImpl<>(permissoes, page, permissoes.size());
	}
	public Page<GrupoPermissao> findAll(Grupo grupo, Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return grupoPermissaoRepository.findAllByGrupo(grupo, pageRequest);
	}

}
