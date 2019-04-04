package com.ebm.pessoal.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.ebm.exceptions.DataIntegrityException;
import com.ebm.exceptions.ObjectNotFoundException;
import com.ebm.pessoal.domain.Cargo;
import com.ebm.pessoal.domain.Funcionario;
import com.ebm.pessoal.domain.Pessoa;
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
		
		if( !cargoService.exist(funcionario.getCargo()))
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
	public void delete(Funcionario funcionario) {
		findById(funcionario.getId());
		funcionarioRepository.delete(funcionario);
	}

	// find
	// -------------------------------------------------------------------------------------------
	public Funcionario findById(Integer id) {
		Optional<Funcionario> funcionario = funcionarioRepository.findById(id);
		return funcionario.orElseThrow(
				() -> new ObjectNotFoundException("Não foi possivel encontrar o funcionario de id: " + id));
	}

	public Funcionario findByCpf(String cpf) {
		return findByPessoa(pessoaService.findbyCPF(cpf));
	}
	

	public Funcionario findByCNPJ(String cnpj) {
		return findByPessoa(pessoaService.findByCPNJ(cnpj));
	}
	
	public Page<Funcionario> findByCargo(Cargo cargo, Integer page, Integer linesPerPage, String orderBy,
			String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return funcionarioRepository.findAllByCargo(cargo, pageRequest);
	}
	
	
	public Page<Funcionario> findByNome(String nome, Integer page, Integer linesPerPage, String orderBy,
			String direction) {

		List<Funcionario> funcionarios = pessoaService.findByNome(nome).stream().map(p -> findByPessoa(p))
				.collect(Collectors.toList());
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return new PageImpl<>(funcionarios, pageRequest, funcionarios.size());

	}

	public Page<Funcionario> findByNomeFantasia(String nome, Integer page, Integer linesPerPage, String orderBy,
			String direction) {
		List<Funcionario> funcionarios = pessoaService.findbyNomeFantasia(nome).stream().map(p -> findByPessoa(p))
				.collect(Collectors.toList());
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return new PageImpl<>(funcionarios, pageRequest, funcionarios.size());

	}

	public Page<Funcionario> findByRazaoSocial(String nome, Integer page, Integer linesPerPage, String orderBy,
			String direction) {
		List<Funcionario> funcionarios = pessoaService.findByRazaoSocial(nome).stream().map(p -> findByPessoa(p))
				.collect(Collectors.toList());
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return new PageImpl<>(funcionarios, pageRequest, funcionarios.size());

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
	public Funcionario findByPessoa(Pessoa pessoa) {
		return funcionarioRepository.findOneByPessoa(pessoa).orElseThrow(
				() -> new ObjectNotFoundException("Não existe um funcionario cadastrado para: " + pessoa.getNome()));
	}

	//aux
	public boolean existWith(Cargo cargo) {
		return funcionarioRepository.countByCargo(cargo) == 0 ? false: true;
	}

}
