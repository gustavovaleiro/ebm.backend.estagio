package com.ebm.pessoal.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.geral.exceptions.DataIntegrityException;
import com.ebm.geral.exceptions.ObjectNotFoundException;
import com.ebm.geral.utils.Utils;
import com.ebm.pessoal.domain.Cargo;
import com.ebm.pessoal.domain.Funcionario;
import com.ebm.pessoal.domain.Pessoa;
import com.ebm.pessoal.domain.TipoPessoa;
import com.ebm.pessoal.dtos.FuncionarioListDTO;
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

	public static final String DATAINTEGRITY_DUPLICATEMATRICULA = DataIntegrityException.DEFAULT
			+ ": Já existe um colaborador com essa mesma matricula.";

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private PessoaService pessoaService;
	@Autowired
	private CargoService cargoService;

	@Transactional
	public Funcionario save(Funcionario funcionario) {
		garantaIntegridade(funcionario);
		saveAssociations(funcionario);
		Utils.audita(funcionario.getHistorico());
		return funcionarioRepository.save(funcionario);
	}

	private void saveAssociations(Funcionario funcionario) {
		funcionario.setPessoa(pessoaService.findById(funcionario.getPessoa().getId()));
		funcionario.setCargo(cargoService.findById(funcionario.getCargo().getId()));
	}

	private void garantaIntegridade(Funcionario funcionario) {
		garantaIntegridadeQuantoPessoa(funcionario);
		garantaIntegridadeQuantoMatricula(funcionario);
		garantaIntegridadeQuantoCargo(funcionario);
	}

	private void garantaIntegridadeQuantoCargo(Funcionario funcionario) {
		if (funcionario.getCargo() == null || funcionario.getCargo().getId() == null)
			throw new DataIntegrityException(DATAINTEGRITY_EMPLOYEWITHOUTOFFICE);
	}

	private void garantaIntegridadeQuantoMatricula(Funcionario funcionario) {
		Optional<Funcionario> funcionarioByMatricula = funcionarioRepository
				.findOneByMatricula(funcionario.getMatricula());
		if (!(funcionario.getMatricula().isEmpty() || funcionario.getMatricula() == null)
				&& (funcionarioByMatricula.isPresent() && !funcionarioByMatricula.get().getId().equals(funcionario.getId())  ))
			throw new DataIntegrityException(DATAINTEGRITY_DUPLICATEMATRICULA);
	}

	private void garantaIntegridadeQuantoPessoa(Funcionario funcionario) {
		if (funcionario.getPessoa() == null)
			throw new DataIntegrityException(DATAINTEGRITY_EMPLOYEWITHOUTPERSON);

		if (funcionario.getPessoa().getId() != null) {
			try {
				Funcionario result = findById(funcionario.getPessoa().getId());
				if (!result.getId().equals(funcionario.getId()))
					throw new DataIntegrityException(DATAINTEGRITY_DUPLICATEPERSON);
			} catch (ObjectNotFoundException ex) {
			}
		}

		if (funcionario.getId() != null && !funcionario.getId().equals(funcionario.getPessoa().getId()) )
			throw new DataIntegrityException(DATAINTEGRITY_CHANCEPERSON);
	}

	@Transactional
	public List<Funcionario> saveAll(List<Funcionario> funcionarios) {
		return funcionarios.stream().map(f -> this.save(f)).collect(Collectors.toList());

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

	public Page<FuncionarioListDTO> findBy(TipoPessoa tipo, String cargoNome, String nome, String matricula,
			PageRequest pageRequest) {

		ExampleMatcher matcher = PessoaService.ExampleMatcherDinamicFilterFor(true, tipo);
		Pessoa pessoa = PessoaService.getPessoa(tipo, nome);
		Cargo cargo = null;
		if (Optional.ofNullable(cargoNome).isPresent())
			cargo = new Cargo(null, cargoNome, null, null);
		Funcionario funcionario = new Funcionario(null, pessoa, matricula, cargo, null, null, null);
		Page<Funcionario> funcionarios = funcionarioRepository.findAll(Example.of(funcionario, matcher), pageRequest);
		List<FuncionarioListDTO> funcionariosDTO = funcionarios.get().map(f -> new FuncionarioListDTO(f))
				.collect(Collectors.toList());

		return new PageImpl<>(funcionariosDTO, pageRequest, funcionariosDTO.size());
	}

	public Funcionario findByCpfOrCnpj(String document) {
		try {
			return findById(pessoaService.findByCpfOrCnpj(document).getId());
		} catch(ObjectNotFoundException ex) {
			throw new ObjectNotFoundException(PessoaService.NOT_FOUND_DOCUMENT + document);
		}
	}

	// aux
	public boolean existWith(Cargo cargo) {
		return funcionarioRepository.countByCargo(cargo) == 0 ? false : true;
	}

	public List<Integer> findIdByNomeLike(String nome) {
		return funcionarioRepository.findAllIdByNomeLike(nome);
	}

	public List<Integer> findIdByEmailPrincipalLike(String email) {
		
		return funcionarioRepository.findAllIdByEmailPrincipalLike(email);
	}
}
