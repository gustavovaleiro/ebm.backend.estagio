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

import com.ebm.exceptions.DataIntegrityException;
import com.ebm.exceptions.ObjectNotFoundException;
import com.ebm.pessoal.domain.Cliente;
import com.ebm.pessoal.domain.Pessoa;
import com.ebm.pessoal.domain.TipoPessoa;
import com.ebm.pessoal.dtos.ClienteListDTO;
import com.ebm.pessoal.repository.ClienteRepository;

@Service
public class ClienteService {

	public static final String DATAINTEGRITY_CLIENTWITHOUTPERSON = DataIntegrityException.DEFAULT
			+ ": Não é possivel salvar um cliente que não possui uma pessoa associada";

	public static final String DATAINTEGRITY_DUPLICATEPERSON = DataIntegrityException.DEFAULT
			+ ": Não é possivel salvar multiplos clientes para uma mesma pessoa";

	public static final String DATAINTEGRITY_CHANCEPERSON = DataIntegrityException.DEFAULT
			+ ": Não é possivel trocar a pessoa que um cliente esta associado.";

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private PessoaService pessoaService;

	@Transactional
	public Cliente save(Cliente cliente) {

		garantaIntegridade(cliente);
		saveAssociations(cliente);
		return clienteRepository.save(cliente);
	}

	private void saveAssociations(Cliente cliente) {
		cliente.setPessoa(pessoaService.save(cliente.getPessoa()));
	}

	private void garantaIntegridade(Cliente cliente) {
		if (cliente.getPessoa() == null)
			throw new DataIntegrityException(DATAINTEGRITY_CLIENTWITHOUTPERSON);

		if (cliente.getPessoa().getId() != null) {// garantir que nao exista outra cliente salvado com a mesma pessoa
			try {
				Cliente result = findById(cliente.getPessoa().getId());
				if (result.getId() != cliente.getId())
					throw new DataIntegrityException(DATAINTEGRITY_DUPLICATEPERSON);
			} catch (ObjectNotFoundException ex) {
			}
		}

		if (cliente.getId() != null && cliente.getId() != cliente.getPessoa().getId())
			throw new DataIntegrityException(DATAINTEGRITY_CHANCEPERSON);
	}

	public void delete(Integer id) {
		clienteRepository.delete(findById(id));
	}

	public void deleteAll() {
		clienteRepository.deleteAll();

	}

	public Cliente findById(Integer id) {
		Optional<Cliente> cliente = clienteRepository.findById(id);
		return cliente
				.orElseThrow(() -> new ObjectNotFoundException("Não foi possivel encontrar o cliente de id: " + id));
	}

	public Page<ClienteListDTO> findBy(TipoPessoa tipo, String nome, PageRequest pageRequest) {

		ExampleMatcher matcher = PessoaService.ExampleMatcherDinamicFilterFor(true, tipo);

		Pessoa pessoa = PessoaService.getPessoa(tipo, nome);

		Cliente cliente = new Cliente(null, pessoa, null, null);

		Page<Cliente> clientes = clienteRepository.findAll(Example.of(cliente, matcher), pageRequest);
		List<ClienteListDTO> clientesDTO = clientes.get().map(c -> new ClienteListDTO(c)).collect(Collectors.toList());

		return new PageImpl<>(clientesDTO, pageRequest, clientesDTO.size());
	}

	public Cliente findByCpfOrCnpj(String document) {
		return findById(pessoaService.findByCpfOrCnpj(document).getId());
	}

	public List<Cliente> saveAll(List<Cliente> clientes) {

		return clientes.stream().map(c -> this.save(c)).collect(Collectors.toList());

	}

}
