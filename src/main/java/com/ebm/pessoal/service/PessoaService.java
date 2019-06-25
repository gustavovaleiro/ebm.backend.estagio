
package com.ebm.pessoal.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import com.ebm.geral.exceptions.DataIntegrityException;
import com.ebm.geral.exceptions.ObjectNotFoundException;
import com.ebm.geral.utils.Utils;
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
	public static final String NEED_EMAIL = DataIntegrityException.DEFAULT
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
		if (!validateCPF(pf.getCpf()))
			throw new DataIntegrityException(INVALID_CPF);

		PessoaFisica result = findbyCPF(pf.getCpf());
		if (result != null && !result.getId().equals(pf.getId()))
			throw new DataIntegrityException(DUPLICATE_CPF);

		saveAssociations(pf);

		pf.setNaturalidade(cidadeService.save(pf.getNaturalidade()));
		if (pf.getRG() != null)
			pf.getRG().setUF(estadoService.save(pf.getRG().getUF()));
		Utils.audita(pf.getHistorico());
		return pessoaFisicaRepository.save(pf);
	}

	@Transactional
	private PessoaJuridica save(PessoaJuridica pj) {
		if (!validateCNPJ(pj.getCnpj()))
			throw new DataIntegrityException(INVALID_CPF);

		PessoaJuridica result = findByCPNJ(pj.getCnpj());
		if (result != null && !result.getId().equals(pj.getId()))
			throw new DataIntegrityException(DUPLICATE_CNPJ);

		saveAssociations(pj);
		Utils.audita(pj.getHistorico());
		return pessoaJuridicaRepository.save(pj);

	}

	public Pessoa save(Pessoa pessoa) {
		return pessoa instanceof PessoaFisica ? save((PessoaFisica) pessoa) : save((PessoaJuridica) pessoa);
	}

	@Transactional
	public List<Pessoa> saveAll(List<Pessoa> pessoas) {
		return pessoas.stream().map(p -> save(p)).collect(Collectors.toList());
	}

	// delete
	// --------------------------------------------------------------------------------------------------------
	public void delete(PessoaFisica pf) {
		findPF(pf.getId());
		deleteAssociations(pf);
		pessoaFisicaRepository.delete(pf);

	}

	public void delete(Pessoa p) {
		if (p instanceof PessoaFisica)
			delete((PessoaFisica) p);
		else
			delete((PessoaJuridica) p);

	}

	private void deleteAssociations(Pessoa p) {
		Optional<List<Endereco>> enderecos = Optional.ofNullable(p.getEndereco());
		Optional<List<Telefone>> telefones = Optional.ofNullable(p.getTelefone());
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
		return pessoaFisicaRepository.findOneByCpf(cpf).orElse(null);
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
		return pessoaJuridicaRepository.findOneByCnpj(cnpj).orElse(null);
	}

	public Pessoa findByCpfOrCnpj(String document) {
		if (!this.validateCPF(document))
			if (!this.validateCNPJ(document))
				throw new DataIntegrityException(" O cpf ou CNPJ passado é invalido");

		Pessoa p = findbyCPF(document);
		if (p == null)
			p = findByCPNJ(document);
		if (p == null)
			throw new ObjectNotFoundException(NOT_FOUND_DOCUMENT + document);
		return p;
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
		if (entidade.size() == 1)
			entidade.iterator().next().setPrincipal(true);
		else if (entidade.stream().filter(e -> e.isPrincipal()).count() > 1)
			throw new DataIntegrityException(MOREONEPRINCIPAL + entidade.get(0).getClass().getSimpleName());
	}

	public boolean validateCPF(String cpf) {
		try {
			CPFValidator cpfValidator = new CPFValidator();
			cpfValidator.assertValid(cpf);
			return true;
		} catch (InvalidStateException e) {
			return false;
		}
	}

	public boolean validateCNPJ(String cnpj) {
		try {
			CNPJValidator cnpjValidator = new CNPJValidator();
			cnpjValidator.assertValid(cnpj);
			return true;
		} catch (InvalidStateException e) {
			return false;
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
		if (tipo == null || tipo.equals(TipoPessoa.PESSOA_FISICA))
			return new PessoaFisica().withNome(nome);
		return new PessoaJuridica().withNome(nome);
	}

}
