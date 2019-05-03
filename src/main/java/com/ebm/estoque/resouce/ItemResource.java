package com.ebm.estoque.resouce;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ebm.estoque.domain.Item;
import com.ebm.estoque.service.interfaces.ItemService;

@RestController
@RequestMapping(value = "/itens")
public class ItemResource {
	@Autowired
	private ItemService itemService;

	@PostMapping
	public ResponseEntity<Item> insert(@RequestBody Item item) {
		Item itemS = itemService.save(item);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(itemS.getId()).toUri();
		return ResponseEntity.created(uri).body(itemS);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<Item> update(@RequestBody Item item, @PathVariable Integer id) {
		item.setId(id);
		item = itemService.save(item);
		return ResponseEntity.ok(item);

	}

}
