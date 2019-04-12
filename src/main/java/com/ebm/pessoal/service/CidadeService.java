package com.ebm.pessoal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.ebm.exceptions.DataIntegrityException;
import com.ebm.pessoal.domain.Cidade;
import com.ebm.pessoal.domain.Estado;
import com.ebm.pessoal.repository.CidadeRepository;

@Service
public class CidadeService {
	
	public static final String CIDADE_ESTADO_INVALIDO = DataIntegrityException.DEFAULT + " cidade esta atrelada a um estado valido";
	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private EstadoService estadoService;
	
	public Cidade save(Cidade cidade) {
		
		if(cidade.getEstado() == null ) {
			throw new DataIntegrityException(CIDADE_ESTADO_INVALIDO);
		}
		estadoService.save(cidade.getEstado());

		return cidadeRepository.save(cidade);
		
	}

	public boolean exist(Estado estado) {
		
		return cidadeRepository.existsByEstado(estado);
	}

	public int count() {
		return (int) cidadeRepository.count();
	}

	public void deleteAll() {
		cidadeRepository.deleteAll();
		
	}

	public void delete(Cidade cidade) {
		cidadeRepository.delete(cidade);
		
	}
	

}
