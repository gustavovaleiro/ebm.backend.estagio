package com.ebm.estoque.service.interfaces;

import org.springframework.stereotype.Service;

import com.ebm.estoque.domain.Fornecedor;
import com.ebm.exceptions.DataIntegrityException;
import com.ebm.exceptions.ObjectNotFoundException;

@Service
public interface FornecedorService {
	String DATAINTEGRITY_FORNECEDORWITHOUTPERSON = DataIntegrityException.DEFAULT
			+ ": é necessario fornecer uma pessoa para o fornecedor";
	String DATAINTEGRITY_DUPLICATEPERSON = DataIntegrityException.DEFAULT
			+ ": Já existe um fornecedor associado a esta pessoa";
	String DATAINTEGRITY_CHANCEPERSON = DataIntegrityException.DEFAULT
			+ ": Não é possivel trocar a pessoa que um fornecedor esta associado.";
	String ONFE_BYID = ObjectNotFoundException.DEFAULT + " um fornecedor com o id: ";
	String DATAINTEGRITY_IDNULL =DataIntegrityException.DEFAULT + ": o id passado é nulo.";

	void deleteAll();

	Fornecedor save(Fornecedor fornecedor);

	Fornecedor findById(Integer id);
}
