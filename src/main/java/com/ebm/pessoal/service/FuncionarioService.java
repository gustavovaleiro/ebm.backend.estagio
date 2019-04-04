package com.ebm.pessoal.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.exceptions.DataIntegrityException;
import com.ebm.exceptions.ObjectNotFoundException;
import com.ebm.pessoal.domain.Cargo;
import com.ebm.pessoal.domain.Funcionario;
import com.ebm.pessoal.dtos.FuncionarioListDTO;
import com.ebm.pessoal.repository.FuncionarioRepository;

@Service
public class FuncionarioService {

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private PessoaService pessoaService;
	@Autowired
	private CargoService cargoService;

	// insert
	// -------------------------------------------------------------------------------------------
	public Funcionario insert(Funcionario funcionario) {
		funcionario.setPessoa(pessoaService.insert(funcionario.getPessoa()));
		funcionario.setId(null);

		if (funcionario.getDataDeAdmissao().isAfter(LocalDate.now()))
			throw new DataIntegrityException("A data de admissão do funcionario nao pode ser futura");

		if (!cargoService.exist(funcionario.getCargo()))
			funcionario.setCargo(cargoService.insert(funcionario.getCargo()));

		return funcionarioRepository.save(funcionario);
	}

	// update
	// -------------------------------------------------------------------------------------------
	public Funcionario update(Funcionario funcionario) {
		findById(funcionario.getId());
		funcionario.setPessoa(pessoaService.update(funcionario.getPessoa()));
		return funcionarioRepository.save(funcionario);
	}

	// delete
	// -------------------------------------------------------------------------------------------
	public void delete(Integer id) {
		funcionarioRepository.delete(findById(id));
	}

	// find
	// -------------------------------------------------------------------------------------------
	public Funcionario findById(Integer id) {
		Optional<Funcionario> funcionario = funcionarioRepository.findById(id);
		return funcionario.orElseThrow(
				() -> new ObjectNotFoundException("Não foi possivel encontrar o funcionario de id: " + id));
	}

	public Funcionario findByCpfOrCnpj(String cpf, String cnpj) {

		return findByPessoa(pessoaService.findByCpfOrCnpj(cpf, cnpj));
	}

	public Page<FuncionarioListDTO> findBy(String tipo, String nome, String nomeFantasia, String razaoSocial,
			String cargo, PageRequest pageRequest) {

		Set<Integer> idsPessoa = pessoaService.getPessoaIdBy(tipo, nome, nomeFantasia, razaoSocial);
		Set<Funcionario> funcionarios = findAllByPessoaId(idsPessoa);
		funcionarios.addAll(findByCargoName(cargo));
		List<FuncionarioListDTO> funcionariosDTO = funcionarios.stream().map(f -> new FuncionarioListDTO(f))
				.collect(Collectors.toList());

		return new PageImpl<>(funcionariosDTO, pageRequest, funcionariosDTO.size());
	}

	public Collection<? extends Funcionario> findByCargoName(String cargo) {

		return funcionarioRepository.findByCargoName(cargo);
	}

	@Transactional
	private Set<Funcionario> findAllByPessoaId(Set<Integer> idsPessoa) {
		// TODO Auto-generated method stub
		return idsPessoa.stream().map(id -> findById(id)).collect(Collectors.toSet());
	}

//	public Page<Funcionario> findByNome(String nome, Integer page, Integer linesPerPage, String orderBy,
//			String direction) {
//		
//		   Set<Pessoa> pessoa = new HashSet<>();
//		   pessoa.addAll(pessoaService.findByNome(nome));
//		   pessoa.addAll(pessoaService.findbyNomeFantasia(nome));
//		   pessoa.addAll(pessoaService.findByRazaoSocial(nome));
//		   
//		   List<Funcionario> funcionarios = pessoa.stream().map( p -> findByPessoa(p)).collect(Collectors.toList());
//		   
//		   PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
//		   
//		  return  new PageImpl<>(funcionarios, pageRequest, funcionarios.size());
//	}
	public Funcionario findByPessoa(Integer id) {
		return funcionarioRepository.findByPessoaId(id)
				.orElseThrow(() -> new ObjectNotFoundException("Não foi possivel encontrar uma pessoa de id: " + id));
	}

	// aux
	public boolean existWith(Cargo cargo) {
		return funcionarioRepository.countByCargo(cargo) == 0 ? false : true;
	}

}
