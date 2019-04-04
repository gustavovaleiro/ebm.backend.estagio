package com.ebm.pessoal.service;

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

import com.ebm.exceptions.ObjectNotFoundException;
import com.ebm.pessoal.domain.Cliente;
import com.ebm.pessoal.domain.TipoPessoa;
import com.ebm.pessoal.dtos.ClienteListDTO;
import com.ebm.pessoal.repository.ClienteRepository;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private PessoaService pessoaService;
	
	//insert -------------------------------------------------------------------------------------------
	public Cliente insert(Cliente cliente) {
		cliente.setPessoa(pessoaService.insert(cliente.getPessoa()));
		cliente.setId(null);
		return clienteRepository.save(cliente);
	}

	//update -------------------------------------------------------------------------------------------
	public Cliente update(Cliente cliente) {
		findById(cliente.getId());
		cliente.setPessoa(pessoaService.update(cliente.getPessoa()));
		return clienteRepository.save(cliente);
	}
	
	//delete -------------------------------------------------------------------------------------------
	public void delete(Integer id) {
		clienteRepository.delete(findById(id));	
	}
	
	//find   -------------------------------------------------------------------------------------------
	public Cliente findById(Integer id) {
		Optional<Cliente> cliente = clienteRepository.findById(id);
		return cliente.orElseThrow(() -> new ObjectNotFoundException("Não foi possivel encontrar o cliente de id: " + id));
	}
	public Page<ClienteListDTO> findBy(String tipo, String nome, String nomeFantasia, String razaoSocial, PageRequest pageRequest) {

		
		Set<Integer> idsPessoa = pessoaService.getPessoaIdBy(tipo,nome,nomeFantasia,razaoSocial);
		
		List<ClienteListDTO> clientes = findAllByPessoaId(idsPessoa).stream().map( c -> new ClienteListDTO(c)).collect(Collectors.toList());
		
		return new PageImpl<>(clientes, pageRequest, clientes.size());
	}
	
	@Transactional
   public List<Cliente> findAllByPessoaId(Collection<Integer> idsPessoa) {
		
		return idsPessoa.stream().map(id -> findByPessoa(id)).collect(Collectors.toList());
	}

   public List<Cliente> findByTipo(TipoPessoa tipo) {
		return pessoaService.findByTipo(tipo).stream().map(p -> findByPessoa(p.getId())).collect(Collectors.toList());
	}
	public Cliente findByCpfOrCnpj(String cpf, String cnpj) {
		
		return findByPessoa( pessoaService.findByCpfOrCnpj(cpf,cnpj));
	}

	//	public Page<Cliente> findByNome(String nome, Integer page, Integer linesPerPage, String orderBy,
//			String direction) {
//		
//		   Set<Pessoa> pessoa = new HashSet<>();
//		   pessoa.addAll(pessoaService.findByNome(nome));
//		   pessoa.addAll(pessoaService.findbyNomeFantasia(nome));
//		   pessoa.addAll(pessoaService.findByRazaoSocial(nome));
//		   
//		   List<Cliente> clientes = pessoa.stream().map( p -> findByPessoa(p)).collect(Collectors.toList());
//		   
//		   PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
//		   
//		  return  new PageImpl<>(clientes, pageRequest, clientes.size());
//	}
	public Cliente findByPessoa(Integer id) {
		return clienteRepository.findByPessoaId(id).orElseThrow(() -> new  ObjectNotFoundException("Não existe uma pessoa de id: " + id));
	}

	

	
	
}
