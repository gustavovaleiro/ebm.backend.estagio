package com.ebm.geral.resource;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ebm.geral.domain.AbstractEntity;
import com.ebm.geral.service.AbstractRestService;

public  abstract class AbstractRestController <ID, Entity extends AbstractEntity<ID>> {
	
	@PostMapping
	public ResponseEntity<Void> insert( @RequestBody Entity entity) {
		Entity obj = this.getService().save(entity);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update( @RequestBody Entity entity,
			@PathVariable ID id) {
		entity.setId(id);
		this.getService().save(entity);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable ID id) {
		this.getService().deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping
	public ResponseEntity<List<Entity>> findAll() {
		return ResponseEntity.ok(this.getService().get());
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Entity> find(@PathVariable ID id) {
		Entity obj = this.getService().findById(id);
		return ResponseEntity.ok(obj);
	}
	
	public abstract AbstractRestService<ID,Entity> getService();
	
}