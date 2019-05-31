package com.ebm.pessoal.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebm.Utils;
import com.ebm.exceptions.DataIntegrityException;
import com.ebm.exceptions.ObjectNotFoundException;
import com.ebm.pessoal.domain.Pessoa;
import com.ebm.pessoal.domain.Telefone;
import com.ebm.pessoal.repository.TelefoneRepository;

@Service
public class TelefoneService {
	@Autowired
	private TelefoneRepository telefoneRepository;
public Telefone save(Telefone telefone) {
		
		try {
			Telefone result = findByDDDAndNumero(telefone.getDDD(), telefone.getNumero());
			if(!result.getId().equals(telefone.getId()))
				throw new DataIntegrityException("O telefone: " + telefone.toString() + " ja existe.");		
		}catch(ObjectNotFoundException e) {}
		Utils.audita(telefone.getHistorico());
		return telefoneRepository.save(telefone);
	}


	public Telefone findByDDDAndNumero(String ddd, String numero) {
	// TODO Auto-generated method stub
	return telefoneRepository.findByDDDAndNumero(ddd,numero).orElseThrow(
			() -> new ObjectNotFoundException  (ObjectNotFoundException.DEFAULT + "telefone "+ (new Telefone(null, ddd, numero, null, false)).toString() )    );
}


	public List<Telefone> saveAll(List<Telefone> telefone) {
		return telefone.stream().map( e -> this.save(e)).collect(Collectors.toList());
	}
	//delete --------------------------------------------------------------------------------------------------------
	public void deleteById(Integer id) {
		find(id);
		telefoneRepository.deleteById(id);
	}
	public void deleteAll(List<Telefone> telefone) {
		telefone.forEach( e -> deleteById(e.getId()));
	}
	public void deleteAll() {
		telefoneRepository.deleteAll();
	}
	public void deleteByPessoaId(Integer id) {
		deleteAll(findByPessoaId(id));
	}
	
	
	//find --------------------------------------------------------------------------------------------------------
	public Telefone find(Integer id) {
		return telefoneRepository.findById(id).orElseThrow( 
				() -> new ObjectNotFoundException("Não foi possivel encontrar o telefone de id: " + id));
	}
	public List<Telefone> findBy(Pessoa pessoa) {
		return findByPessoaId(pessoa.getId());
		
	}
	public List<Telefone> findByPessoaId(Integer id) {
		List<Telefone> telefones = telefoneRepository.findByPessoa(id);
		
		if(telefones.size() == 0 || telefones == null) 
			throw new ObjectNotFoundException("A pessoa de id: " + id  + " não possui telefone cadastrado");

		return telefones;
	}
	
	public List<String> getTipoTelefone(){
		return telefoneRepository.findAllTipoTelefone();
	}

}


