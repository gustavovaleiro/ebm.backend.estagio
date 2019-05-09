package com.ebm.estoque.service.interfaces;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ebm.estoque.domain.CategoriaItem;
import com.ebm.estoque.domain.Fornecedor;
import com.ebm.estoque.dtos.FornecedorListDTO;
import com.ebm.exceptions.DataIntegrityException;
import com.ebm.exceptions.ObjectNotFoundException;
import com.ebm.pessoal.domain.TipoPessoa;

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

	void deleteAll();

	Fornecedor save(Fornecedor fornecedor);

	Fornecedor findById(Integer id);

	Fornecedor findByCpfOrCnpj(String cnpj);

	List<Fornecedor> saveAll(List<Fornecedor> fornecedores);

	Page<FornecedorListDTO> findBy(TipoPessoa pessoaFisica, String nome, Set<Integer> categorias,
			PageRequest pageRequest);
}
