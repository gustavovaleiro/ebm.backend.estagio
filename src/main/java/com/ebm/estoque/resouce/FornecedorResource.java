package com.ebm.estoque.resouce;

import java.net.URI;
import java.util.Set;

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

import com.ebm.estoque.domain.Fornecedor;
import com.ebm.estoque.dtos.FornecedorListDTO;
import com.ebm.estoque.service.interfaces.FornecedorService;
import com.ebm.pessoal.domain.TipoPessoa;

@RestController
@RequestMapping(value = "/fornecedors")
public class FornecedorResource {
	@Autowired
	private FornecedorService fornecedorService;

	@PreAuthorize("hasAuthority('FORNECEDOR_POST')")
	@PostMapping
	public ResponseEntity<Void> insert(@RequestBody Fornecedor fornecedor) {
		Fornecedor obj = fornecedorService.save(fornecedor);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@PreAuthorize("hasAuthority('FORNECEDOR_PUT')")
	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@RequestBody Fornecedor fornecedor, @PathVariable Integer id) {
		fornecedor.setId(id);
		fornecedor = fornecedorService.save(fornecedor);
		return ResponseEntity.noContent().build();

	}

	@PreAuthorize("hasAuthority('FORNECEDOR_DELETE')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		fornecedorService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAuthority('FORNECEDOR_GET')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<Fornecedor> find(@PathVariable Integer id) {
		Fornecedor obj = fornecedorService.findById(id);
		return ResponseEntity.ok(obj);
	}

	@PreAuthorize("hasAuthority('FORNECEDOR_GET')")
	@GetMapping(value = "/documents")
	public ResponseEntity<Fornecedor> findBy(@RequestParam(value = "value", required = true) final String document) {

		return ResponseEntity.ok(fornecedorService.findByCpfOrCnpj(document));
	}

	@PreAuthorize("hasAuthority('FORNECEDOR_GET')")
	@GetMapping(value = "/page")
	public ResponseEntity<Page<FornecedorListDTO>> findAllBy(
			@RequestParam(value = "nome", required = false) String nome,
			@RequestParam(value = "tipo", required = false) String tipo,
			@RequestParam(value = "categorias", required = false) Set<Integer> categorias,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "10") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {

		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Page<FornecedorListDTO> rs = fornecedorService.findBy(TipoPessoa.fromString(tipo), nome, categorias,
				pageRequest);
		return ResponseEntity.ok().body(rs);
	}

}
