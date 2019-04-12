

package com.ebm.pessoal.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.ebm.exceptions.DataIntegrityException;
import com.ebm.exceptions.ObjectNotFoundException;
import com.ebm.pessoal.domain.Email;
import com.ebm.pessoal.domain.Endereco;
import com.ebm.pessoal.domain.Pessoa;
import com.ebm.pessoal.domain.PessoaFisica;
import com.ebm.pessoal.domain.PessoaJuridica;
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
	public static final String NOT_FIND_ID = "não foi possivel encontrar a pessoa de id: ";
	public static final String DUPLICATE_CPF = "Ja existe uma pessoa com esse cpf, se você deseja alterar dados, modifique a pessoa ja existente";
	public static final String DUPLICATE_CNPJ = "Ja existe uma pessoa com esse cnpj, se você deseja alterar dados, modifique a pessoa ja existente";
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

	// insert
	// --------------------------------------------------------------------------------------------------------
	@Transactional
	private PessoaFisica save(PessoaFisica pf) {
		validateCPF(pf.getCpf());

		try {
			findbyCPF(pf.getCpf());
			throw new DataIntegrityException(
					DUPLICATE_CPF);
		} catch (ObjectNotFoundException e) {
		
			pf.setDataCadastro(LocalDateTime.now());
			
			saveAssociations(pf);

			cidadeService.save(pf.getNaturalidade());

			return pessoaFisicaRepository.save(pf);
		}

	}

	@Transactional
	private PessoaJuridica save(PessoaJuridica pj) {
		pj.setId(null);
		validateCNPJ(pj.getCnpj());
		try {
			findByCPNJ(pj.getCnpj());
			throw new DataIntegrityException(
					DUPLICATE_CNPJ);
		} catch (ObjectNotFoundException e) {
			pj.setDataCadastro(LocalDateTime.now());
			saveAssociations(pj);
			
			return pessoaJuridicaRepository.save(pj);
		}
	}

	public Pessoa save(Pessoa pessoa) {
		return pessoa.getTipo() == TipoPessoa.PESSOAFISICA ? save((PessoaFisica) pessoa)
				: save((PessoaJuridica) pessoa);
	}




	// delete
	// --------------------------------------------------------------------------------------------------------
	public void delete(PessoaFisica pf) {
		findPF(pf.getId());
		deleteAssociations(pf);
		pessoaFisicaRepository.delete(pf);
		
	}

	private void deleteAssociations(Pessoa p) {
		Optional<List<Email>> emails = Optional.ofNullable(p.getEmail());
		Optional<List<Endereco>> enderecos = Optional.ofNullable(p.getEndereco());
		Optional<List<Telefone>> telefones = Optional.ofNullable(p.getTelefone());
		enderecos.ifPresent(o -> enderecoService.deleteAll(o));
		emails.ifPresent(o -> emailService.deleteAll(o));
		telefones.ifPresent(o -> telefoneService.deleteAll(o));
		
	}

	public void delete(PessoaJuridica pj) {
		findPJ(pj.getId());
		pessoaJuridicaRepository.delete(pj);
	}

	public void deleteById(Integer id) {
		try {
			findPF(id);
			pessoaFisicaRepository.deleteById(id);
		} catch (ObjectNotFoundException e) {
			findPJ(id);
			this.pessoaJuridicaRepository.deleteById(id);

		}
	}

	// find
	// --------------------------------------------------------------------------------------------------------
	public Pessoa findById(Integer id) {
		Optional<PessoaFisica> pf = pessoaFisicaRepository.findById(id);

		if (pf.isPresent())
			return pf.get();

		return pessoaJuridicaRepository.findById(id)
				.orElseThrow(() -> new ObjectNotFoundException(NOT_FIND_ID + id));
	}

	public PessoaFisica findPF(Integer id) {
		if (id == null)
			throw new NullPointerException();
		Optional<PessoaFisica> pf = pessoaFisicaRepository.findById(id);
		return pf.orElseThrow(() -> new ObjectNotFoundException(NOT_FIND_ID + id));
	}

	public Set<Pessoa> findAllByNome(String nome) {
		Set<Pessoa> pessoas = new HashSet<>();
		pessoas.addAll(findByNome(nome));
		pessoas.addAll(findbyNomeFantasia(nome));
		return pessoas;
	}

	public List<PessoaFisica> findByNome(String nome) {
		return pessoaFisicaRepository.findAllByNomeLikeIgnoreCase(nome);
	}

	public PessoaFisica findbyCPF(String cpf) {
		return pessoaFisicaRepository.findOneByCpf(cpf).orElseThrow(
				() -> new ObjectNotFoundException(NOT_FIND_CPF + cpf));
	}

	public List<PessoaFisica> findPFByEmail(String email) {
		return pessoaFisicaRepository.findAllByEmailLike(email);
	}

	public List<PessoaFisica> findByRG(RG rg) {
		return pessoaFisicaRepository.findAllByRG(Example.of(rg));
	}

	public PessoaJuridica findPJ(Integer id) {
		Optional<PessoaJuridica> pj = pessoaJuridicaRepository.findById(id);
		return pj.orElseThrow(() -> new ObjectNotFoundException(NOT_FIND_ID + id));
	}

	public PessoaJuridica findByCPNJ(String cnpj) {
		return pessoaJuridicaRepository.findOneByCnpj(cnpj).orElseThrow(
				() -> new ObjectNotFoundException(NOT_FIND_CNPJ + cnpj));
	}

	public List<PessoaJuridica> findbyNomeFantasia(String nome) {
		return pessoaJuridicaRepository.findAllByNomeLikeIgnoreCase(nome);
	}

	public List<PessoaJuridica> findByRazaoSocial(String nome) {
		return pessoaJuridicaRepository.findAllByRazaoSocialIgnoreCaseContaining(nome);
	}

	public List<PessoaJuridica> findByInscricaoEstadual(String inscricaoEstadual) {

		return pessoaJuridicaRepository.findAllByInscricaoEstadualIgnoreCaseContaining(inscricaoEstadual);
	}

	public List<PessoaJuridica> findByInscricaoMunicipal(String inscricaoEstadual) {
		return pessoaJuridicaRepository.findAllByInscricaoEstadualIgnoreCaseContaining(inscricaoEstadual);
	}

	public List<? extends Pessoa> findByTipo(TipoPessoa tipo) {
		if (tipo == TipoPessoa.PESSOAFISICA)
			return pessoaFisicaRepository.findAll();

		return pessoaJuridicaRepository.findAll();
	}

	public Set<Integer> getPessoaIdBy(String tipo, String nome, String nomeFantasia, String razaoSocial) {

		Set<Integer> ids = new HashSet<>();

		if (tipo.equals(TipoPessoa.PESSOAFISICA.getDescricao())) {
			ids.addAll(pessoaFisicaRepository.findIdOfAll());

			if (!nome.equals(""))
				ids.retainAll(pessoaFisicaRepository.findIdOfAllWithNameContain(nome));

		} else if (tipo.equals(TipoPessoa.PESSOAJURIDICA.getDescricao())) {
			ids.addAll(pessoaJuridicaRepository.findIdOfAll());

			if (!nomeFantasia.equals(""))
				ids.retainAll(pessoaJuridicaRepository.findIdOfAllWithNameContain(nomeFantasia));
			if (!razaoSocial.equals(""))
				ids.retainAll(pessoaJuridicaRepository.findIdOfAllWithRazaoSocialContain(razaoSocial));

		} else
			throw new DataIntegrityException("É necessario fornecer um tipo de pessoa para essa busca");

		return ids;
	}

	public Integer findByCpfOrCnpj(String cpf, String cnpj) {
		try {
			return findbyCPF(cpf).getId();
		} catch (ObjectNotFoundException e) {
			return findByCPNJ(cnpj).getId();
		}
	}

	public Pessoa findByDocument(Pessoa pessoa) {

		if (pessoa.getTipo() == TipoPessoa.PESSOAFISICA) {
			try {
				return findbyCPF(((PessoaFisica) pessoa).getCpf());
			} catch (ObjectNotFoundException e) {
			}
		} else {
			try {
				return findByCPNJ(((PessoaJuridica) pessoa).getCnpj());

			} catch (ObjectNotFoundException e) {
			}
		}
		return null;
	}

	// aux
	// --------------------------------------------------------------------------------------------------------
	private void saveAssociations(Pessoa p) {
		Optional<List<Email>> emails = Optional.ofNullable(p.getEmail());
		Optional<List<Endereco>> enderecos = Optional.ofNullable(p.getEndereco());
		Optional<List<Telefone>> telefones = Optional.ofNullable(p.getTelefone());
		enderecos.ifPresent(
				o -> p.setEndereco(enderecoService.salveAll(p.getEndereco())));
		emails.ifPresent(o -> p.setEmail(emailService.saveAll(p.getEmail())));
		telefones.ifPresent(
				o -> p.setTelefone(telefoneService.saveAll(p.getTelefone())));

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
		if(b) {
			pessoaFisicaRepository.deleteAll();
			pessoaJuridicaRepository.deleteAll();
		}
		
	}

}
