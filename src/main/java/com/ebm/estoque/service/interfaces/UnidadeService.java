package com.ebm.estoque.service.interfaces;

import org.springframework.stereotype.Service;

import com.ebm.estoque.domain.Unidade;
import com.ebm.exceptions.DataIntegrityException;
import com.ebm.exceptions.ObjectNotFoundException;

@Service
public interface UnidadeService {

	String DATAINTEGRITY_ABREVNULL = DataIntegrityException.DEFAULT + ": Uma unidade deve ter uma abreviação";
	String DATAINTEGRITY_NOMENULL = DataIntegrityException.DEFAULT + ": Uma unidade deve ter um nome";
	String DATAINTEGRITY_DUPLICATEABRE = DataIntegrityException.DEFAULT + ": Já existe uma unidade com essa abreviação";
	String ONFE_NOTFOUNDBYID = ObjectNotFoundException.DEFAULT + " uma unidade com o id passado";
	String ONFE_NOTFOUNDBYABREV = ObjectNotFoundException.DEFAULT + " uma unidade com a abreviação passada";

	
	void deleteAll();

	Unidade save(Unidade unidade);

	Unidade findByAbrev(String unidade);

}
