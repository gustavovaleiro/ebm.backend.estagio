package com.ebm.estoque.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.estoque.domain.Fornecedor;
import com.ebm.estoque.repository.FornecedorRepository;
import com.ebm.estoque.service.interfaces.FornecedorService;
import com.ebm.exceptions.DataIntegrityException;
import com.ebm.exceptions.ObjectNotFoundException;
import com.ebm.pessoal.service.PessoaService;

@Service
public class FornecedorServiceImpl  implements FornecedorService{
	
	@Autowired
	FornecedorRepository fornecedorRepository;
	@Autowired
	PessoaService pessoaService;
	
	@Transactional
	public Fornecedor save(Fornecedor fornecedor) {
		garantaIntegridade(fornecedor);
		saveAssociations(fornecedor);
		return fornecedorRepository.save(fornecedor);
	}

	private void saveAssociations(Fornecedor fornecedor) {
		fornecedor.setPessoa(pessoaService.save(fornecedor.getPessoa()));
	}

	private void garantaIntegridade(Fornecedor fornecedor) {
		if (fornecedor.getPessoa() == null)
			throw new DataIntegrityException(DATAINTEGRITY_FORNECEDORWITHOUTPERSON);

		if (fornecedor.getPessoa().getId() != null) {
			try {
				Fornecedor result = findById(fornecedor.getPessoa().getId());
				if (result.getId() != fornecedor.getId())
					throw new DataIntegrityException(DATAINTEGRITY_DUPLICATEPERSON);
			} catch (ObjectNotFoundException ex) {
			}
		}

		if (fornecedor.getId() != null && fornecedor.getId() != fornecedor.getPessoa().getId())
			throw new DataIntegrityException(DATAINTEGRITY_CHANCEPERSON);
	}


	public Fornecedor findById(Integer id) {
		if(!Optional.ofNullable(id).isPresent())
			throw new DataIntegrityException(DATAINTEGRITY_IDNULL);
		return fornecedorRepository.findById(id).orElseThrow( () -> new ObjectNotFoundException(ONFE_BYID+ id));
	}

	@Override
	public void deleteAll() {
		fornecedorRepository.deleteAll();
	}

	
}
