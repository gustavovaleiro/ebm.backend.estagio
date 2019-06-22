package com.ebm.pessoal.resource;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ebm.pessoal.domain.Pessoa;
import com.ebm.pessoal.service.PessoaService;

@RestController
@RequestMapping(value = "/pessoas")
public class PessoaResource {
	@Autowired
	private PessoaService pessoaService;
	@PreAuthorize("hasAnyAuthority('FUNCIONARIO_POST','CLIENTE_POST','FORNECEDOR_POST')")
	@PostMapping
	public ResponseEntity<Void> insert(@Valid @RequestBody Pessoa pessoa){
		Pessoa obj = pessoaService.save(pessoa);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(obj.getId()).toUri();
		
		return ResponseEntity.created(uri).build();
	}
	@PreAuthorize("hasAnyAuthority('FUNCIONARIO_PUT','CLIENTE_PUT','FORNECEDOR_PUT')")
	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@Valid @RequestBody Pessoa pessoa, @PathVariable Integer id){
		pessoa.setId(id);
		pessoa = pessoaService.save(pessoa);
		return ResponseEntity.noContent().build();
		
	}
	@PreAuthorize("hasAnyAuthority('FUNCIONARIO_GET','CLIENTE_GET','FORNECEDOR_GET')")
	@GetMapping(value="/{id}")
	public ResponseEntity<Pessoa> find(@PathVariable Integer id) {
		Pessoa obj = pessoaService.findById(id);
		return ResponseEntity.ok(obj);
	}
	@PreAuthorize("hasAnyAuthority('FUNCIONARIO_DELETE','CLIENTE_DELETE','FORNECEDOR_DELETE')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		pessoaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
}
