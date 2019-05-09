package com.ebm.estoque.service.interfaces;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.ebm.estoque.domain.CategoriaItem;
import com.ebm.exceptions.DataIntegrityException;
import com.ebm.exceptions.ObjectNotFoundException;

@Service
public interface CategoriaItemService {
	
	String DATAINTEGRITY_NOMENULL = DataIntegrityException.DEFAULT + ": Uma categoria deve ter um nome";
	String DATAINTEGRITY_DUPLICATENOME = DataIntegrityException.DEFAULT + ": Já existe uma categoria com esse nome";
	String ONFE_NOTFOUNDBYID = ObjectNotFoundException.DEFAULT + " uma categoria com o id passado";
	String ONFE_NOTFOUNDBYNOME = ObjectNotFoundException.DEFAULT + " uma categoria com o nome passado";
	
	
	void deleteAll();

	CategoriaItem save(CategoriaItem categoria);

	CategoriaItem findByNome(String categoria);

	Set<CategoriaItem> findAllById(Set<Integer> ids);

	CategoriaItem findById(Integer id);

	List<CategoriaItem> saveAll(List<CategoriaItem> categorias);

}
