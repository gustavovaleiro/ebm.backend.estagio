package com.ebm.pessoal.resource;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ebm.exceptions.DataIntegrityException;
import com.ebm.pessoal.domain.Cliente;
import com.ebm.pessoal.dtos.ClienteListDTO;
import com.ebm.pessoal.service.ClienteService;


@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {
	@Autowired
	private ClienteService clienteService;	
	
	@PostMapping
	public ResponseEntity<Void> insert( @RequestBody Cliente cliente){
		Cliente obj = clienteService.save(cliente);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@RequestBody Cliente cliente, @PathVariable Integer id){
		cliente.setId(id);
		cliente = clienteService.save(cliente);
		return ResponseEntity.noContent().build();
		
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		clienteService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<Cliente> find(@PathVariable Integer id) {
		Cliente obj = clienteService.findById(id);
		return ResponseEntity.ok(obj);
	}
	
	@GetMapping(value="/documents")
	public ResponseEntity<Cliente> findBy( 
			@RequestParam(value ="cpf", defaultValue="", required = false) final String cpf,
			@RequestParam(value = "cnpj", defaultValue="", required = false) final String cnpj){
		
		Cliente cli;
		if(!cpf.equals("") || !cnpj.equals("")) 
			cli = clienteService.findByCpfOrCnpj(cpf, cnpj);
		 else 
			throw new DataIntegrityException("Não foi passado dados");
		
		return ResponseEntity.ok(cli);
	}

	@GetMapping(value="/page")
	public ResponseEntity<Page<ClienteListDTO>> findAllBy(
			@RequestParam(value ="nome", defaultValue="null") String nome,
			@RequestParam(value ="tipo", defaultValue="null") String tipo,
			@RequestParam(value ="nomeFantasia", defaultValue="null") String nomeFantasia,
			@RequestParam(value ="razaoSocial", defaultValue="null") String razaoSocial,
			@RequestParam(value ="page", defaultValue="0") Integer page,
			@RequestParam(value ="linesPerPage", defaultValue="10")Integer linesPerPage,
			@RequestParam(value ="orderBy", defaultValue="nome")String orderBy,
			@RequestParam(value ="direction", defaultValue="ASC")String direction ) {
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Page<ClienteListDTO> rs = clienteService.findBy(tipo, nome, pageRequest);
		return ResponseEntity.ok().body(rs);
	}
	
	
}
