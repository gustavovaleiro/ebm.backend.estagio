package com.ebm.pessoal.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebm.exceptions.ObjectNotFoundException;
import com.ebm.pessoal.domain.Cargo;
import com.ebm.pessoal.domain.Funcionario;
import com.ebm.pessoal.repository.FuncionarioRepository;

@Service
public class FuncionarioService {

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private PessoaService pessoaService;
	@Autowired
	private CargoService cargoService;

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
