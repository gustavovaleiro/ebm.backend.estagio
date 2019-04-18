package com.ebm.pessoal.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.exceptions.DataIntegrityException;
import com.ebm.exceptions.ObjectNotFoundException;
import com.ebm.pessoal.domain.Cliente;
import com.ebm.pessoal.repository.ClienteRepository;

@Service
public class ClienteService {

	public static final String DATAINTEGRITY_CLIENTWITHOUTPERSON = DataIntegrityException.DEFAULT + ": Não é possivel salvar um cliente que não possui uma pessoa associada";

	public static final String DATAINTEGRITY_DUPLICATEPERSON = DataIntegrityException.DEFAULT + ": Não é possivel salvar multiplos clientes para uma mesma pessoa";

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private PessoaService pessoaService;
	
	@Transactional
	public Cliente save(Cliente cliente) {
		
		if(cliente.getPessoa() == null)
			throw new DataIntegrityException(DATAINTEGRITY_CLIENTWITHOUTPERSON);
		
		if(cliente.getPessoa().getId() != null) {//garantir que nao exista outra cliente salvado com a mesma pessoa
			try {
				Cliente result = findById(cliente.getPessoa().getId());
				if(result.getId() != cliente.getId())
					throw new DataIntegrityException(DATAINTEGRITY_DUPLICATEPERSON);
			}catch(ObjectNotFoundException ex) { }
		}
			
		
		cliente.setPessoa(pessoaService.save(cliente.getPessoa()));
		return clienteRepository.save(cliente);
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

//	public Page<ClienteListDTO> findBy(String tipo, String nome, String nomeFantasia, String razaoSocial,
//			PageRequest pageRequest) {
//
//		Set<Integer> idsPessoa = pessoaService.getPessoaIdBy(tipo, nome, nomeFantasia, razaoSocial);
//
//		List<ClienteListDTO> clientes = findAllByPessoaId(idsPessoa).stream().map(c -> new ClienteListDTO(c))
//				.collect(Collectors.toList());
//
//		return new PageImpl<>(clientes, pageRequest, clientes.size());
//	}



	public Cliente findByCpfOrCnpj(String cpf, String cnpj) {

		return findById(pessoaService.findByCpfOrCnpj(cpf, cnpj));
	}






}
