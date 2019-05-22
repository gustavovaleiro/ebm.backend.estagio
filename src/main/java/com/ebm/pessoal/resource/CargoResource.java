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

import com.ebm.pessoal.domain.Cargo;
import com.ebm.pessoal.service.CargoService;

@RestController
@RequestMapping(value = "/cargos")
public class CargoResource {
	@Autowired
	private CargoService cargoService;

	@PostMapping
	public ResponseEntity<Void> insert(@RequestBody Cargo cargo) {
		Cargo obj = cargoService.save(cargo);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@RequestBody Cargo cargo, @PathVariable Integer id) {
		cargo.setId(id);
		cargo = cargoService.save(cargo);
		return ResponseEntity.noContent().build();

	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		cargoService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Cargo> find(@PathVariable Integer id) {
		Cargo obj = cargoService.findById(id);
		return ResponseEntity.ok(obj);
	} 


	@GetMapping(value = "/page")
	public ResponseEntity<Page<Cargo>> findAllBy(@RequestParam(value = "nome", required = false) String nome,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "10") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {

		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Page<Cargo> rs = cargoService.findByName(nome,pageRequest);
		return ResponseEntity.ok().body(rs);
	}

}
