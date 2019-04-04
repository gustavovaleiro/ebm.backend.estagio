package com.ebm.pessoal.resource;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
	
	@PostMapping
	public ResponseEntity<Void> insert( @RequestBody Pessoa pessoa){
		Pessoa obj = pessoaService.insert(pessoa);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@RequestBody Pessoa pessoa, @PathVariable Integer id){
		pessoa.setId(id);
		pessoa = pessoaService.update(pessoa);
		return ResponseEntity.noContent().build();
		
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		pessoaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
}
