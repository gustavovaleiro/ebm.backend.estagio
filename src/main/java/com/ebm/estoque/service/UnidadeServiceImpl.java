package com.ebm.estoque.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.ebm.estoque.domain.Unidade;
import com.ebm.estoque.repository.UnidadeRepository;
import com.ebm.estoque.service.interfaces.UnidadeService;
import com.ebm.geral.exceptions.DataIntegrityException;
import com.ebm.geral.exceptions.ObjectNotFoundException;
import com.ebm.geral.service.AbstractRestService;

@Service
public class UnidadeServiceImpl extends AbstractRestService<Integer, Unidade>implements UnidadeService{

	@Autowired
	private UnidadeRepository unidadeRepository;


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


	@Override
	public void deleteAll() {
		unidadeRepository.deleteAll();
		
	}



	@Override
	public boolean validateEntityForSave(Unidade unidade) {
		this.garantaIntegridade(unidade);
		return true;
	}

	@Override
	public JpaRepository<Unidade, Integer> getRepository() {
		return this.unidadeRepository;
	}

	

}
