package com.ebm.estoque.service.interfaces;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ebm.estoque.domain.CategoriaItem;
import com.ebm.exceptions.DataIntegrityException;
import com.ebm.exceptions.ObjectNotFoundException;

@Service
public interface CategoriaItemService {
	
	String DATAINTEGRITY_NOMENULL = DataIntegrityException.DEFAULT + ": Uma categoria deve ter um nome";
	String DATAINTEGRITY_DUPLICATENOME = DataIntegrityException.DEFAULT + ": Já existe uma categoria com esse nome";
	String ONFE_NOTFOUNDBYID = ObjectNotFoundException.DEFAULT + " uma categoria com o id ";
	String ONFE_NOTFOUNDBYNOME = ObjectNotFoundException.DEFAULT + " uma categoria com o nome passado";
	String DATAINTEGRITY_CATTHASITEM  = DataIntegrityException.DEFAULT + ": Existem itens atrelados a essa categoria, nao é possivel excluir";
	String DATAINTEGRITY_IDNULL =  DataIntegrityException.DEFAULT + ": o id passado nao pode ser nulo";
	void deleteAll();
	void deleteById(Integer id);

	CategoriaItem save(CategoriaItem categoria);

	CategoriaItem findByNome(String categoria);
	Page<CategoriaItem> findByNome(String categoria, PageRequest page);

	Set<CategoriaItem> findAllById(Set<Integer> ids);

	CategoriaItem findById(Integer id);

	List<CategoriaItem> saveAll(List<CategoriaItem> categorias);

}
