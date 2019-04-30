package com.ebm.estoque.service;

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
			if(un.getId() != categoria.getId())
				throw new DataIntegrityException(DATAINTEGRITY_DUPLICATENOME);
			}catch(ObjectNotFoundException ex) {}
		
		if(categoria.getNome() == null || categoria.getNome().isEmpty())
			throw new DataIntegrityException(DATAINTEGRITY_NOMENULL);
	}

	

	public CategoriaItem findByNome(String abrev) {
		return categoriaRepository.findByNome(abrev).orElseThrow( () -> new ObjectNotFoundException(ONFE_NOTFOUNDBYNOME));
		
	}

	public CategoriaItem findById(Integer id) {
		return categoriaRepository.findById(id).orElseThrow( () -> new ObjectNotFoundException(ONFE_NOTFOUNDBYID));
		
	}

	@Override
	public void deleteAll() {
		categoriaRepository.deleteAll();
		
	}

	

}
