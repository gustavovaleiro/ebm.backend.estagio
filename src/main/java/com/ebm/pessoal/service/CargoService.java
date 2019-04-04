package com.ebm.pessoal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebm.exceptions.DataIntegrityException;
import com.ebm.exceptions.ObjectNotFoundException;
import com.ebm.pessoal.domain.Cargo;
import com.ebm.pessoal.repository.CargoRepository;

@Service
public class CargoService {
	@Autowired
	private CargoRepository cargoRepository;
	@Autowired
	private FuncionarioService funcionarioService;
	
	
	//insert
	public Cargo insert(Cargo cargo) {
		cargo.setId(null);
		return cargoRepository.save(cargo);
	}

	
	//update
	public Cargo update(Cargo cargo) {
		findById(cargo.getId());
		return cargoRepository.save(cargo);
		
	}	
	

	//delete 
	  public void delete(Cargo cargo) {
		  findById(cargo.getId());
		  if(funcionarioService.existWith(cargo)) 
			  cargoRepository.delete(cargo);
		  else
			  throw new DataIntegrityException("Não foi possivel excluir o cargo: "+ cargo.getNomeCargo() + " pois existe(m) funionario(s) atrelado(s) a ele");
	  }
	
	
	//find
	public Cargo findById(Integer id) {
		return cargoRepository.findById(id).orElseThrow( () -> new ObjectNotFoundException("Não foi possivel encontrar cargo de id: " + id));
	}

	//aux
	public boolean exist(Cargo cargo) {
		return cargoRepository.existsById(cargo.getId());
	}


}
