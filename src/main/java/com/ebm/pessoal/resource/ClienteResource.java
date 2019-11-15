package com.ebm.pessoal.resource;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.ebm.pessoal.domain.Cliente;
import com.ebm.pessoal.domain.TipoPessoa;
import com.ebm.pessoal.dtos.ClienteListDTO;
import com.ebm.pessoal.service.ClienteService;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {
	@Autowired
	private ClienteService clienteService;

	@PreAuthorize("hasAuthority('CLIENTE_POST')")
	@PostMapping
	public ResponseEntity<Void> insert(@Valid @RequestBody Cliente cliente) {
		Cliente obj = clienteService.save(cliente);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@PreAuthorize("hasAuthority('CLIENTE_PUT')")
	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@Valid @RequestBody Cliente cliente, @PathVariable Integer id) {
		cliente.setId(id);
		cliente = clienteService.save(cliente);
		return ResponseEntity.noContent().build(); 

	}

	@PreAuthorize("hasAuthority('CLIENTE_DELETE')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		clienteService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAuthority('CLIENTE_GET')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<Cliente> find(@PathVariable Integer id) {
		Cliente obj = clienteService.findById(id);
		return ResponseEntity.ok(obj);
	}

	@PreAuthorize("hasAuthority('CLIENTE_GET')")
	@GetMapping(value = "/document")
	public ResponseEntity<Cliente> findBy(@RequestParam(value = "value", required = true) final String document) {

		return ResponseEntity.ok(clienteService.findByCpfOrCnpj(document));
	}

	@PreAuthorize("hasAuthority('CLIENTE_GET')")
	@GetMapping(value = "/page")
	public ResponseEntity<Page<ClienteListDTO>> findAllBy(
			@RequestParam(value = "nome", required = false) String nome,
			@RequestParam(value = "tipo", required = false) String tipo,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "10") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {

		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Page<ClienteListDTO> rs = clienteService.findBy(TipoPessoa.fromString(tipo), nome, pageRequest);
		return ResponseEntity.ok().body(rs);
	}

}
