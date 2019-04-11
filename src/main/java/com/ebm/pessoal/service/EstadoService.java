package com.ebm.pessoal.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.ebm.exceptions.ObjectNotFoundException;
import com.ebm.pessoal.domain.Estado;
import com.ebm.pessoal.repository.EstadoRepository;

@Service
public class EstadoService {
	public static final String NOTFOUND_UF = ObjectNotFoundException.DEFAULT +  " um estado com uf: ";
	public static final String NOTFOUND_ID =  ObjectNotFoundException.DEFAULT + " um estado com id: ";
	@Autowired
	private EstadoRepository estadoRepository;
	
	// insert & update

	public Estado save(Estado estado) {
			Optional<Estado> estadoR = estadoRepository.findOneByUF(estado.getUF());
			
			if(estadoR.isPresent()) 
				estado.setId(estadoR.get().getId());
			
			return estadoRepository.save(estado);
	
	}
	
	//finds
	public Estado findByUf(String uf) {
		return estadoRepository.findOneByUF(uf).
				orElseThrow(() -> new ObjectNotFoundException(NOTFOUND_UF + uf));
	}
	
	public Estado find(Integer id) {
		return estadoRepository.findById(id).
				orElseThrow(() -> new ObjectNotFoundException(NOTFOUND_ID + id));
	}

	
	//delets
	public void deleteAll() {
		estadoRepository.deleteAll();
		
	}

	
	//others
	public boolean existe(Estado estado) {
		return estadoRepository.exists(Example.of(estado));
	}

	public Long count() {
		return estadoRepository.count();
	}

	public void delete(Integer id) {
		find(id);
		estadoRepository.deleteById(id);	
	}

	public void deleteByUf(String uf) {
		estadoRepository.delete(findByUf(uf));
		
	}



}
