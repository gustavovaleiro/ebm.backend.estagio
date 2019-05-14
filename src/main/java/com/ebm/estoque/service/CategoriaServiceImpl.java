package com.ebm.estoque.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebm.estoque.domain.CategoriaItem;
import com.ebm.estoque.repository.CategoriaItemRepository;
import com.ebm.estoque.service.interfaces.CategoriaItemService;
import com.ebm.exceptions.DataIntegrityException;
import com.ebm.exceptions.ObjectNotFoundException;

@Service
public class CategoriaServiceImpl implements CategoriaItemService{

	@Autowired
	private CategoriaItemRepository categoriaRepository;
	
	
	@Override
	public CategoriaItem save(CategoriaItem categoria) {
		garantaIntegridade(categoria);
		saveAssociacoes(categoria);
		return categoriaRepository.save(categoria);
	}
	
	private void saveAssociacoes(CategoriaItem categoria) {
	}

	private void garantaIntegridade(CategoriaItem categoria) {
		
		try {
			CategoriaItem un = findByNome(categoria.getNome());
			if(!un.getId().equals(categoria.getId()))
				throw new DataIntegrityException(DATAINTEGRITY_DUPLICATENOME);
			}catch(ObjectNotFoundException ex) {}
		
		if(categoria.getNome() == null || categoria.getNome().isEmpty())
			throw new DataIntegrityException(DATAINTEGRITY_NOMENULL);
	}

	

	public CategoriaItem findByNome(String nome) {
		if(nome == null)
			throw new DataIntegrityException(DATAINTEGRITY_NOMENULL);
		return categoriaRepository.findByNomeIgnoreCaseLike(nome).orElseThrow( () -> new ObjectNotFoundException(ONFE_NOTFOUNDBYNOME));
		
	}

	public CategoriaItem findById(Integer id) {
		return categoriaRepository.findById(id).orElseThrow( () -> new ObjectNotFoundException(ONFE_NOTFOUNDBYID));
		
	}
	
	@Override
	public Set<CategoriaItem> findAllById(Set<Integer> ids) {
		return categoriaRepository.findAllById(ids).stream().collect(Collectors.toSet());
	}
	

	@Override
	public void deleteAll() {
		categoriaRepository.deleteAll();
		
	}

	@Override
	public List<CategoriaItem> saveAll(List<CategoriaItem> categorias) {
		// TODO Auto-generated method stub
		return categorias.stream().map(c -> this.save(c)).collect(Collectors.toList());
	}

	

	

}
