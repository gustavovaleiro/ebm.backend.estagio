package com.ebm.estoque.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ebm.estoque.domain.CategoriaItem;
import com.ebm.estoque.dtos.ItemListDTO;
import com.ebm.estoque.repository.CategoriaItemRepository;
import com.ebm.estoque.service.interfaces.CategoriaItemService;
import com.ebm.estoque.service.interfaces.ItemService;
import com.ebm.geral.exceptions.DataIntegrityException;
import com.ebm.geral.exceptions.ObjectNotFoundException;
import com.ebm.geral.utils.Utils;

@Service
public class CategoriaServiceImpl implements CategoriaItemService{

	@Autowired
	private CategoriaItemRepository categoriaRepository;
	@Autowired
	private ItemService itens;
	
	@Override
	public CategoriaItem save(@Valid CategoriaItem categoria) {
		garantaIntegridade(categoria);
		saveAssociacoes(categoria);
		Utils.audita(categoria.getHistorico());
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
	public Page<CategoriaItem> findPageBy(String nome, PageRequest request) {
		
		CategoriaItem cat = new CategoriaItem(null,nome);
		ExampleMatcher matcher = Utils.getExampleMatcherForDinamicFilter(true);
		
		return this.categoriaRepository.findAll(Example.of(cat,matcher), request);
	}

	public CategoriaItem findById(Integer id) {
		if(!Optional.ofNullable(id).isPresent())
			throw new DataIntegrityException(DATAINTEGRITY_IDNULL);
		return categoriaRepository.findById(id).orElseThrow( () -> new ObjectNotFoundException(ONFE_NOTFOUNDBYID + id));
		
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

	@Override
	public void deleteById(Integer id) {
		CategoriaItem cat = findById(id);
		
		if(!itens.findBy(null, null, null, null, cat.getNome()).isEmpty()){
			throw new DataIntegrityException(DATAINTEGRITY_CATTHASITEM);
		}
		categoriaRepository.deleteById(id);
		
	}

	@Override
	public Page<CategoriaItem> findByNome(String nome, Pageable _page) {
		System.out.println(nome);
		Page<CategoriaItem> page = categoriaRepository.findByNomeIgnoreCaseLike("%"+nome+"%",_page);
		System.out.println(page.getNumberOfElements());
		return page;
	}

	

	

}
