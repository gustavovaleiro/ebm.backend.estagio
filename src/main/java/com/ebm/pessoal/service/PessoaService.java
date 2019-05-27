
package com.ebm.pessoal.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import com.ebm.Utils;
import com.ebm.exceptions.DataIntegrityException;
import com.ebm.exceptions.ObjectNotFoundException;
import com.ebm.pessoal.domain.Email;
import com.ebm.pessoal.domain.Endereco;
import com.ebm.pessoal.domain.Pessoa;
import com.ebm.pessoal.domain.PessoaFisica;
import com.ebm.pessoal.domain.PessoaJuridica;
import com.ebm.pessoal.domain.Principalizar;
import com.ebm.pessoal.domain.RG;
import com.ebm.pessoal.domain.Telefone;
import com.ebm.pessoal.domain.TipoPessoa;
import com.ebm.pessoal.repository.PessoaFisicaRepository;
import com.ebm.pessoal.repository.PessoaJuridicaRepository;

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;

@Service
public class PessoaService {
	public static final String INVALID_CNPJ = "Não foi possivel validar esse cnpj, tente novamente com um cnpj valido";
	public static final String INVALID_CPF = "Não foi possivel validar esse cpf, tente novamente com um cpf valido";
	public static final String NOT_FIND_CNPJ = "Não foi possivel encontrar uma pessoa com o cnpj: ";
	public static final String NOT_FIND_CPF = "Não foi possivel encontrar uma pessoa com o cpf: ";
	public static final String NOT_FOUND_ID = "não foi possivel encontrar a pessoa de id: ";
	public static final String DUPLICATE_CPF = "Ja existe uma pessoa com esse cpf, se você deseja alterar dados, modifique a pessoa ja existente";
	public static final String DUPLICATE_CNPJ = "Ja existe uma pessoa com esse cnpj, se você deseja alterar dados, modifique a pessoa ja existente";
	public static final String NEED_ADDRESS = DataIntegrityException.DEFAULT
			+ ": É necessario pelo menos um endereco para cadastrar uma pessoa.";
	public static final String NEED_PHONE = DataIntegrityException.DEFAULT
			+ ": É necessario pelo menos um telefone para cadastrar uma pessoa.";
	public static final String NOT_FOUND_DOCUMENT = "Não foi possivel encontrar uma pessoa com o documento nº: ";
	public static final String NEED_EMAIL =  DataIntegrityException.DEFAULT
			+ ": É necessario pelo menos um email para cadastrar uma pessoa.";
	public static final String MOREONEPRINCIPAL = DataIntegrityException.DEFAULT
	+ ": Uma pessoa só pode possuir um atributo principal do tipo ";

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;
	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;
	@Autowired
	private EnderecoService enderecoService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private TelefoneService telefoneService;
	@Autowired
	private CidadeService cidadeService;
	@Autowired
	private EstadoService estadoService;
	
	// insert
	// --------------------------------------------------------------------------------------------------------
	@Transactional
	private PessoaFisica save(PessoaFisica pf) {
		validateCPF(pf.getCpf());
		Boolean update = false;
		
		try {
			PessoaFisica result = findbyCPF(pf.getCpf());
			if (!result.getId().equals(pf.getId()))
				throw new DataIntegrityException(DUPLICATE_CPF);
			else
				update = true;
		} catch (ObjectNotFoundException e) {}
		
		if(update)
			pf.setDataUltimaModificacao(LocalDateTime.now());
		else
			pf.setDataCadastro(LocalDateTime.now());

		saveAssociations(pf);

		pf.setNaturalidade(cidadeService.save(pf.getNaturalidade()));
		if(pf.getRG()!=null)
			pf.getRG().setUF(estadoService.save(pf.getRG().getUF()));
		return pessoaFisicaRepository.save(pf);

	}

	@Transactional
	private PessoaJuridica save(PessoaJuridica pj) {
		validateCNPJ(pj.getCnpj());
		Boolean update = false;
		try {
			PessoaJuridica result= findByCPNJ(pj.getCnpj());
			if(!result.getId().equals(pj.getId()))
				throw new DataIntegrityException(DUPLICATE_CNPJ);
			else
				update = true;
		} catch (ObjectNotFoundException e) {}
		if(update)
			pj.setDataUltimaModificacao(LocalDateTime.now());
		else
			pj.setDataCadastro(LocalDateTime.now());
		
		saveAssociations(pj);
		return pessoaJuridicaRepository.save(pj);
		
	}

	public Pessoa save(Pessoa pessoa) {
		return pessoa.getTipo() == TipoPessoa.PESSOA_FISICA ? save((PessoaFisica) pessoa)
				: save((PessoaJuridica) pessoa);
	}
	
	@Transactional
	public List<Pessoa> saveAll(List<Pessoa> pessoas) {
		return	pessoas.stream().map( p -> save(p)).collect(Collectors.toList());
	}
	// delete
	// --------------------------------------------------------------------------------------------------------
	public void delete(PessoaFisica pf) {
		findPF(pf.getId());
		deleteAssociations(pf);
		pessoaFisicaRepository.delete(pf);

	}

	private void deleteAssociations(Pessoa p) {
		Optional<List<Endereco>> enderecos = Optional.ofNullable(p.getEndereco());
		Optional<List<Telefone>> telefones= Optional.ofNullable(p.getTelefone());
		Optional<List<Email>> emails = Optional.ofNullable(p.getEmail());
		enderecos.ifPresent(o -> enderecoService.deleteAll(o));
		emails.ifPresent(o -> emailService.deleteAll(o));
		telefones.ifPresent(o -> telefoneService.deleteAll(o));

	}

	public void delete(PessoaJuridica pj) {
		findPJ(pj.getId());
		deleteAssociations(pj);
		pessoaJuridicaRepository.delete(pj);
	}

	public void deleteById(Integer id) {
		Pessoa pessoa;
		try {
			pessoa = findPF(id);
			pessoaFisicaRepository.deleteById(id);
		} catch (ObjectNotFoundException e) {

			pessoa = findPJ(id);
			this.pessoaJuridicaRepository.deleteById(id);

		}
		deleteAssociations(pessoa);

	}

	// find
	// --------------------------------------------------------------------------------------------------------
	public Pessoa findById(Integer id) {
		Optional<PessoaFisica> pf = pessoaFisicaRepository.findById(id);

		if (pf.isPresent())
			return pf.get();

		return pessoaJuridicaRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(NOT_FOUND_ID + id));
	}

	public PessoaFisica findPF(Integer id) {
		if (id == null)
			throw new NullPointerException();
		Optional<PessoaFisica> pf = pessoaFisicaRepository.findById(id);
		return pf.orElseThrow(() -> new ObjectNotFoundException(NOT_FOUND_ID + id));
	}

	private PessoaFisica findbyCPF(String cpf) {
		return pessoaFisicaRepository.findOneByCpf(cpf)
				.orElseThrow(() -> new ObjectNotFoundException(NOT_FIND_CPF + cpf));
	}

	public List<PessoaFisica> findPFByEmail(String email) {
		return pessoaFisicaRepository.findAllByEmailLike(email);
	}

	public List<PessoaFisica> findByRG(RG rg) {
		return pessoaFisicaRepository.findAllByRG(Example.of(rg));
	}

	public PessoaJuridica findPJ(Integer id) {
		Optional<PessoaJuridica> pj = pessoaJuridicaRepository.findById(id);
		return pj.orElseThrow(() -> new ObjectNotFoundException());
	}

	private PessoaJuridica findByCPNJ(String cnpj) {
		return pessoaJuridicaRepository.findOneByCnpj(cnpj)
				.orElseThrow(() -> new ObjectNotFoundException());
	}

	public Pessoa findByCpfOrCnpj(String document) {
		try {
			return findbyCPF(document);
		} catch (ObjectNotFoundException e) {
			try {
				return findByCPNJ(document);
			}catch (ObjectNotFoundException ex) {
				throw new ObjectNotFoundException(NOT_FOUND_DOCUMENT + document);
			}
			
		}
	}


	// aux
	// --------------------------------------------------------------------------------------------------------
	

	private void saveAssociations(Pessoa p) {
		Optional<List<Endereco>> enderecos = Optional.ofNullable(p.getEndereco());
		Optional<List<Telefone>> telefones = Optional.ofNullable(p.getTelefone());
		Optional<List<Email>> emails = Optional.ofNullable(p.getEmail());
		
		if (!enderecos.isPresent() || enderecos.get().size() == 0)
			throw new DataIntegrityException(NEED_ADDRESS);
		if (!telefones.isPresent() || telefones.get().size() == 0)
			throw new DataIntegrityException(NEED_PHONE);
		if (!emails.isPresent() || emails.get().size() == 0)
			throw new DataIntegrityException(NEED_EMAIL);
		
		integridadeAssociacaoEPrincipal(enderecos.get());
		integridadeAssociacaoEPrincipal(telefones.get());
		integridadeAssociacaoEPrincipal(emails.get());
		
		
		
		p.setEndereco(enderecoService.salveAll(p.getEndereco()));
		p.setEmail(emailService.saveAll(p.getEmail()));
		p.setTelefone(telefoneService.saveAll(p.getTelefone()));

	}
	
	private void integridadeAssociacaoEPrincipal(List<? extends Principalizar> entidade) {
		if(entidade.size() == 1)
			entidade.iterator().next().setPrincipal(true);
		else
			if(entidade.stream().filter(e -> e.isPrincipal()).count() > 1)
				throw new DataIntegrityException(MOREONEPRINCIPAL + entidade.get(0).getClass().getSimpleName());
	}

	public void validateCPF(String cpf) {
		try {
			CPFValidator cpfValidator = new CPFValidator();
			cpfValidator.assertValid(cpf);
		} catch (InvalidStateException e) {
			throw new DataIntegrityException(INVALID_CPF);
		}
	}

	public void validateCNPJ(String cnpj) {
		try {
			CNPJValidator cnpjValidator = new CNPJValidator();
			cnpjValidator.assertValid(cnpj);
		} catch (InvalidStateException e) {
			throw new DataIntegrityException(INVALID_CNPJ);
		}
	}

	public void deleteAll(boolean b) {
		pessoaFisicaRepository.deleteAll();
		pessoaJuridicaRepository.deleteAll();
		if (b) {
			
			enderecoService.deleteAll();
			telefoneService.deleteAll();
			emailService.deleteAll();
		}

	}

	public static ExampleMatcher ExampleMatcherDinamicFilterFor(Boolean ignoreCase, TipoPessoa tipo) {
		ExampleMatcher matcher = Utils.getExampleMatcherForDinamicFilter(ignoreCase);
		if (tipo == null)
			matcher = matcher.withIgnorePaths("pessoa.tipo");
		return matcher;
	}

	public static Pessoa getPessoa(TipoPessoa tipo, String nome) {
		if(tipo == null || tipo.equals(TipoPessoa.PESSOA_FISICA))
			return new PessoaFisica().withNome(nome);
		return new PessoaJuridica().withNome(nome);
	}


}
