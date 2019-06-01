package com.ebm.pessoal.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.ebm.geral.exceptions.DataIntegrityException;
import com.ebm.geral.exceptions.ObjectNotFoundException;
import com.ebm.pessoal.domain.Estado;
import com.ebm.pessoal.repository.EstadoRepository;

@Service
public class EstadoService {
	public static final String NOTFOUND_UF = ObjectNotFoundException.DEFAULT +  " um estado com uf: ";
	public static final String NOTFOUND_ID =  ObjectNotFoundException.DEFAULT + " um estado com id: ";
	public static final String DATAINTEGRITY_ETADOCOMCIDADE = DataIntegrityException.DEFAULT +": o estado possui cidades cadastradas";
	public static final String UF_INVALIDO = DataIntegrityException.DEFAULT+ ": estado deve possuir uf com duas letras.";
	@Autowired
	private EstadoRepository estadoRepository;
	@Autowired
	private CidadeService cidadeService;
	
	// insert & update

	public Estado save(Estado estado) {
			Optional<Estado> estadoR = estadoRepository.findOneByUF(estado.getUF());
			
			if(estadoR.isPresent()) {
				estado.setId(estadoR.get().getId());
			}
				
			if(estado.getUF().length() != 2)
				throw new DataIntegrityException(UF_INVALIDO);
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
	public void deleteAll(boolean deletarMesmoComCidade) {
		if(!deletarMesmoComCidade && cidadeService.count() > 0) {
			throw new DataIntegrityException(DATAINTEGRITY_ETADOCOMCIDADE);
		}
		
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
		Estado estado = find(id);
		if(cidadeService.exist(estado))
			throw new DataIntegrityException(DATAINTEGRITY_ETADOCOMCIDADE);
		estadoRepository.deleteById(id);	
	}

	public void deleteByUf(String uf) {
		Estado estado = findByUf(uf);
		if(cidadeService.exist(estado))
			throw new DataIntegrityException(DATAINTEGRITY_ETADOCOMCIDADE);
		estadoRepository.delete(estado);
		
	}



}
