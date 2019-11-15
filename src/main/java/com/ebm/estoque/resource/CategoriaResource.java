package com.ebm.estoque.resource;

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

import com.ebm.estoque.domain.CategoriaItem;
import com.ebm.estoque.service.interfaces.CategoriaItemService;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {
	@Autowired
	private CategoriaItemService categoriaService;

	@PreAuthorize("hasAuthority('ITEM_AUX_POST')")
	@PostMapping
	public ResponseEntity<Void> insert (@Valid @RequestBody CategoriaItem categoria) {
		CategoriaItem categoriaS = categoriaService.save(categoria);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(categoriaS.getId())
				.toUri();
		return ResponseEntity.created(uri).build();
	}

	@PreAuthorize("hasAuthority('ITEM_AUX_PUT')")
	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@Valid @RequestBody CategoriaItem categoria, @PathVariable Integer id) {
		categoria.setId(id);
		categoria = categoriaService.save(categoria);
		return ResponseEntity.noContent().build();

	}

	@PreAuthorize("hasAuthority('ITEM_AUX_DELETE')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete( @PathVariable Integer id) {
		categoriaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAuthority('ITEM_AUX_GET')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<CategoriaItem> find( @PathVariable Integer id) {
		CategoriaItem obj = categoriaService.findById(id);
		return ResponseEntity.ok(obj);
	}

	@PreAuthorize("hasAuthority('ITEM_AUX_GET')")
	@GetMapping(value = "/page")
	public ResponseEntity<Page<CategoriaItem>> findAllBy( @RequestParam(value = "nome", required = false) String nome,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "10") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Page<CategoriaItem> rs = categoriaService.findByNome(nome, pageRequest);
		return ResponseEntity.ok().body(rs);
	}

}
