package com.ebm.estoque.resouce;

import java.net.URI;

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

import com.ebm.estoque.domain.Item;
import com.ebm.estoque.dtos.ItemListDTO;
import com.ebm.estoque.service.interfaces.ItemService;

@RestController
@RequestMapping(value = "/itens")
public class ItemResource {
	@Autowired
	private ItemService itemService;

	@PreAuthorize("hasAuthority('ITEM_POST')")
	@PostMapping
	public ResponseEntity<Item> insert(@RequestBody Item item) {
		Item itemS = itemService.save(item);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(itemS.getId()).toUri();
		return ResponseEntity.created(uri).body(itemS);
	}

	@PreAuthorize("hasAuthority('ITEM_PUT')")
	@PutMapping(value = "/{id}")
	public ResponseEntity<Item> update(@RequestBody Item item, @PathVariable Integer id) {
		item.setId(id);
		item = itemService.save(item);
		return ResponseEntity.ok(item);

	}

	@PreAuthorize("hasAuthority('ITEM_DELETE')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		itemService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAuthority('ITEM_GET')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<Item> find(@PathVariable Integer id) {
		Item obj = itemService.findById(id);
		return ResponseEntity.ok(obj);
	}

	@PreAuthorize("hasAuthority('ITEM_GET')")
	@GetMapping(value = "/page")
	public ResponseEntity<Page<ItemListDTO>> findAllBy(
			@RequestParam(value = "codInterno", required = false) String codInterno,
			@RequestParam(value = "nome", required = false) String nome,
			@RequestParam(value = "tipo", required = false) String tipo,
			@RequestParam(value = "unidade", required = false) String unidade,
			@RequestParam(value = "categoria", required = false) String categoria,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "10") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Page<ItemListDTO> rs = itemService.findBy(codInterno, tipo, nome, unidade, categoria, pageRequest);
		return ResponseEntity.ok().body(rs);
	}

}
