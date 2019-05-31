package com.ebm.estoque.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebm.Utils;
import com.ebm.estoque.domain.Unidade;
import com.ebm.estoque.repository.UnidadeRepository;
import com.ebm.estoque.service.interfaces.ItemService;
import com.ebm.estoque.service.interfaces.UnidadeService;
import com.ebm.exceptions.DataIntegrityException;
import com.ebm.exceptions.ObjectNotFoundException;

@Service
public class UnidadeServiceImpl implements UnidadeService{

	@Autowired
	private UnidadeRepository unidadeRepository;
	@Autowired
	private ItemService itens;
	
	@Override
	public Unidade save(Unidade unidade) {
		garantaIntegridade(unidade);
		saveAssociacoes(unidade);
		Utils.audita(unidade.getHistorico());
		return unidadeRepository.save(unidade);
	}
	
	private void saveAssociacoes(Unidade unidade) {
	}

	private void garantaIntegridade(Unidade unidade) {
		if(unidade.getAbrev() == null || unidade.getAbrev().isEmpty())
			throw new DataIntegrityException(DATAINTEGRITY_ABREVNULL);
		
		try {
			Unidade un = findByAbrev(unidade.getAbrev());
			if(!un.getId().equals(unidade.getId()))
				throw new DataIntegrityException(DATAINTEGRITY_DUPLICATEABRE);
			}catch(ObjectNotFoundException ex) {}
		
		if(unidade.getNome() == null || unidade.getNome().isEmpty())
			throw new DataIntegrityException(DATAINTEGRITY_NOMENULL);
	}

	

	public Unidade findByAbrev(String abrev) {
		return unidadeRepository.findByAbrevIgnoreCaseLike(abrev).orElseThrow( () -> new ObjectNotFoundException(ONFE_NOTFOUNDBYABREV));
		
	}

	public Unidade findById(Integer id) {
		if(!Optional.ofNullable(id).isPresent())
			throw new DataIntegrityException(DATAINTEGRITY_IDNULL);
		return unidadeRepository.findById(id).orElseThrow( () -> new ObjectNotFoundException(ONFE_NOTFOUNDBYID + id));
		
	}

	@Override
	public void deleteAll() {
		unidadeRepository.deleteAll();
		
	}

	@Override
	public void deleteById(Integer id) {
		Unidade un = findById(id);
		if(!itens.findBy(null, null, null, un.getAbrev(), null).isEmpty()){
			throw new DataIntegrityException(DATAINTEGRITY_UNITHASITEM);
		}
		unidadeRepository.deleteById(id);
		
	}

	

}
