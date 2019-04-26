package com.ebm.pessoal.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.exceptions.DataIntegrityException;
import com.ebm.exceptions.ObjectNotFoundException;
import com.ebm.pessoal.domain.Cargo;
import com.ebm.pessoal.domain.Funcionario;
import com.ebm.pessoal.repository.FuncionarioRepository;

@Service
public class FuncionarioService {

	public static final String DATAINTEGRITY_EMPLOYEWITHOUTPERSON = DataIntegrityException.DEFAULT
			+ ": O colaborador não possui pessoa associada";

	public static final String DATAINTEGRITY_DUPLICATEPERSON = DataIntegrityException.DEFAULT
			+ ": Não é possivel salvar multiplos colaboradores para uma mesma pessoa";

	public static final String DATAINTEGRITY_EMPLOYEWITHOUTOFFICE = DataIntegrityException.DEFAULT
			+ ": O colaborador não possui um cargo associado";

	public static final String DATAINTEGRITY_CHANCEPERSON = DataIntegrityException.DEFAULT
			+ ": Não é possivel trocar a pessoa que um colaborador esta associado.";

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private PessoaService pessoaService;
	@Autowired
	private CargoService cargoService;

	@Transactional
	public Funcionario save(Funcionario funcionario) {
		if (funcionario.getPessoa() == null)
			throw new DataIntegrityException(DATAINTEGRITY_EMPLOYEWITHOUTPERSON);

		if (funcionario.getPessoa().getId() != null) {// garantir que nao exista outra funcionario salvado com a mesma
														// pessoa
			try {
				Funcionario result = findById(funcionario.getPessoa().getId());
				if (result.getId() != funcionario.getId())
					throw new DataIntegrityException(DATAINTEGRITY_DUPLICATEPERSON);
			} catch (ObjectNotFoundException ex) {}
		} 
		
		
		if(funcionario.getId() != null && funcionario.getId() != funcionario.getPessoa().getId())
			throw new DataIntegrityException(DATAINTEGRITY_CHANCEPERSON);
		
		funcionario.setPessoa(pessoaService.save(funcionario.getPessoa()));
		if (funcionario.getCargo() == null)
			throw new DataIntegrityException(DATAINTEGRITY_EMPLOYEWITHOUTOFFICE);
		
		funcionario.setCargo(cargoService.save(funcionario.getCargo()));
		return funcionarioRepository.save(funcionario);
	}
	
	@Transactional
	public List<Funcionario> saveAll(List<Funcionario> asList) {
		// TODO Auto-generated method stub
		
	}
	// delete
	// -------------------------------------------------------------------------------------------
	public void delete(Integer id) {
		funcionarioRepository.delete(findById(id));
	}

	public void deleteAll() {
		funcionarioRepository.deleteAll();

	}

	// find
	// -------------------------------------------------------------------------------------------
	public Funcionario findById(Integer id) {
		Optional<Funcionario> funcionario = funcionarioRepository.findById(id);
		return funcionario.orElseThrow(
				() -> new ObjectNotFoundException("Não foi possivel encontrar o funcionario de id: " + id));
	}

//	public Funcionario findByCpfOrCnpj(String cpf, String cnpj) {
//
//		return findByPessoa(pessoaService.findByCpfOrCnpj(cpf, cnpj));
//	}
//
//	public Page<FuncionarioListDTO> findBy(String tipo, String nome, String nomeFantasia, String razaoSocial,
//			String cargo, PageRequest pageRequest) {
//		
//	}
//
//	public Collection<? extends Funcionario> findByCargoName(String cargo) {
//
//		return funcionarioRepository.findByCargoName(cargo);
//	}

	// aux
	public boolean existWith(Cargo cargo) {
		return funcionarioRepository.countByCargo(cargo) == 0 ? false : true;
	}

	

}
