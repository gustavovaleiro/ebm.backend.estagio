package com.ebm.pessoal.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.ebm.exceptions.ObjectNotFoundException;
import com.ebm.pessoal.domain.Cliente;
import com.ebm.pessoal.domain.Pessoa;
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
	public void delete(Cliente cliente) {
		findById(cliente.getId());
		clienteRepository.delete(cliente);	
	}
	
	//find   -------------------------------------------------------------------------------------------
	public Cliente findById(Integer id) {
		Optional<Cliente> cliente = clienteRepository.findById(id);
		return cliente.orElseThrow(() -> new ObjectNotFoundException("Não foi possivel encontrar o cliente de id: " + id));
	}
	public Cliente findByCpf(String cpf) {
		return findByPessoa(pessoaService.findbyCPF(cpf));
	}
	public Cliente findByCNPJ(String cnpj) {
		return findByPessoa(pessoaService.findByCPNJ(cnpj));
	}
	public Page<Cliente> findByNome(String nome, Integer page, Integer linesPerPage, String orderBy,
			String direction) {
		
		   List<Cliente>  clientes = pessoaService.findByNome(nome).stream().map(p -> findByPessoa(p)).collect(Collectors.toList());
		   PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		   return  new PageImpl<>(clientes, pageRequest, clientes.size());
		
	}
	public Page<Cliente> findByNomeFantasia(String nome, Integer page, Integer linesPerPage, String orderBy,
			String direction) {
		   List<Cliente>  clientes = pessoaService.findbyNomeFantasia(nome).stream().map(p -> findByPessoa(p)).collect(Collectors.toList());
		   PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		   return  new PageImpl<>(clientes, pageRequest, clientes.size());
		
	}
	public Page<Cliente> findByRazaoSocial(String nome, Integer page, Integer linesPerPage, String orderBy,
			String direction) {
		   List<Cliente>  clientes = pessoaService.findByRazaoSocial(nome).stream().map(p -> findByPessoa(p)).collect(Collectors.toList());
		   PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		   return  new PageImpl<>(clientes, pageRequest, clientes.size());
		
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
	public Cliente findByPessoa(Pessoa pessoa) {
		return clienteRepository.findOneByPessoa(pessoa).orElseThrow(() -> new  ObjectNotFoundException("Não existe um cliente cadastrado para: " + pessoa.getNome()));
	}
	
}
