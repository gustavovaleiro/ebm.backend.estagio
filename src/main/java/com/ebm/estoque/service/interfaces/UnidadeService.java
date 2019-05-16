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
	String ONFE_NOTFOUNDBYID = ObjectNotFoundException.DEFAULT + " uma unidade com o id ";
	String ONFE_NOTFOUNDBYABREV = ObjectNotFoundException.DEFAULT + " uma unidade com a abreviação passada";
	String DATAINTEGRITY_IDNULL = DataIntegrityException.DEFAULT + ": o id passado nao pode ser nulo";
	String DATAINTEGRITY_UNITHASITEM = DataIntegrityException.DEFAULT + ": Existem itens atrelados a essa unidade, nao é possivel excluir";
	void deleteAll();

	Unidade save(Unidade unidade);

	Unidade findByAbrev(String unidade);

	void deleteById(Integer id);

	Unidade findById(Integer id);


}
