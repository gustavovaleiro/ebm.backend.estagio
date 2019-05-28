package com.ebm.pessoal.service.interfaces;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ebm.exceptions.DataIntegrityException;
import com.ebm.exceptions.ObjectNotFoundException;
import com.ebm.pessoal.domain.Fornecedor;
import com.ebm.pessoal.domain.TipoPessoa;
import com.ebm.pessoal.dtos.FornecedorListDTO;

@Service
public interface FornecedorService {
	String DATAINTEGRITY_FORNECEDORWITHOUTPERSON = DataIntegrityException.DEFAULT
			+ ": é necessario fornecer uma pessoa para o fornecedor";
	String DATAINTEGRITY_DUPLICATEPERSON = DataIntegrityException.DEFAULT
			+ ": Já existe um fornecedor associado a esta pessoa";
	String DATAINTEGRITY_CHANCEPERSON = DataIntegrityException.DEFAULT
			+ ": Não é possivel trocar a pessoa que um fornecedor esta associado.";
	String ONFE_BYID = ObjectNotFoundException.DEFAULT + " um fornecedor com o id: ";
	String DATAINTEGRITY_IDNULL = DataIntegrityException.DEFAULT + ": o id passado é nulo.";
	String DATAINTEGRITY_FORNECEDORHASCATEGORIA = DataIntegrityException.DEFAULT + ": não é possivel excluir um fornecedor com categoria associada.";

	void deleteAll();

	Fornecedor save(Fornecedor fornecedor);

	Fornecedor findById(Integer id);

	Fornecedor findByCpfOrCnpj(String cnpj);

	List<Fornecedor> saveAll(List<Fornecedor> fornecedores);

	Page<FornecedorListDTO> findBy(TipoPessoa tipo, String nome, Set<Integer> categorias,
			PageRequest pageRequest);

	void delete(Integer id);
}
