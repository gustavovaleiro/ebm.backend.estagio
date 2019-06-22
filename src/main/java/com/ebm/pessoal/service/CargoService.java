package com.ebm.pessoal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ebm.geral.exceptions.DataIntegrityException;
import com.ebm.geral.exceptions.ObjectNotFoundException;
import com.ebm.geral.utils.Utils;
import com.ebm.pessoal.domain.Cargo;
import com.ebm.pessoal.repository.CargoRepository;

@Service
public class CargoService {
	public static final String ONFE_BYNAME = ObjectNotFoundException.DEFAULT+" um cargo com o nome dado";
	public static final String DATAINTEGRITY_NOTNAME = DataIntegrityException.DEFAULT + ": Um cargo deve ter um nome";
	public static final String ONFE_BYID = ObjectNotFoundException.DEFAULT + " cargo de id: ";
	@Autowired
	private CargoRepository cargoRepository;
	@Autowired
	private FuncionarioService funcionarioService;

	
	public Cargo save(Cargo c1) {
		if(c1.getId() != null) {
			findById(c1.getId());
		}
		if(c1.getNomeCargo() == null || c1.getNomeCargo().isEmpty())
			throw new DataIntegrityException(DATAINTEGRITY_NOTNAME);
		Utils.audita(c1.getHistorico());
		return cargoRepository.save(c1);
	}
	
	// delete
	public void deleteAll( ) {
		cargoRepository.deleteAll();
	}
	public void delete(Integer id) {
		Cargo cargo = findById(id);
		if (!funcionarioService.existWith(cargo))
			cargoRepository.delete(cargo);
		else
			throw new DataIntegrityException("Não foi possivel excluir o cargo: " + cargo.getNomeCargo()
					+ " pois existe(m) funionario(s) atrelado(s) a ele");
	}

	// find
	public Cargo findById(Integer id) {
		return cargoRepository.findById(id)
				.orElseThrow(() -> new ObjectNotFoundException(ONFE_BYID + id));
	}

	public Page<Cargo> findByName(String nomeCargo, PageRequest pageRequest) {
		Page<Cargo> cargos;
		if(nomeCargo!=null) {
			cargos = cargoRepository.findByNomeCargoContainsIgnoreCase(nomeCargo, pageRequest);
			if(cargos.isEmpty())
				throw new ObjectNotFoundException(ONFE_BYNAME);
		} else
			cargos = cargoRepository.findAll(pageRequest);
			
	 return cargos;
	}

}
