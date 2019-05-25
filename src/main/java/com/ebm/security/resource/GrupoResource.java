package com.ebm.security.resource;

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

import com.ebm.security.Grupo;
import com.ebm.security.dto.GrupoDTO;
import com.ebm.security.service.GrupoService;

@RestController
@RequestMapping(value = "/grupos")
public class GrupoResource {
	@Autowired
	private GrupoService grupoService;

	@PreAuthorize("hasAuthority('GRUPO_PERMISSAO_POST')")
	@PostMapping
	public ResponseEntity<Void> insert(@RequestBody Grupo grupo) {
		Grupo obj = grupoService.insert(grupo);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@PreAuthorize("hasAuthority('GRUPO_PERMISSAO_PUT')")
	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@RequestBody Grupo grupo, @PathVariable Integer id) {
		grupo.setId(id);
		grupo = grupoService.update(grupo);
		return ResponseEntity.noContent().build();

	}

	@PreAuthorize("hasAuthority('GRUPO_PERMISSAO_DELETE')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		grupoService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAuthority('GRUPO_PERMISSAO_GET')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<Grupo> find(@PathVariable Integer id) {
		Grupo obj = grupoService.find(id);
		return ResponseEntity.ok(obj);
	}

	@PreAuthorize("hasAuthority('GRUPO_PERMISSAO_GET')")
	@GetMapping(value = "/")
	public ResponseEntity<Page<GrupoDTO>> findAllBy(@RequestParam(value = "nome", defaultValue = "") String nome,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "10") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {

		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Page<GrupoDTO> rs = grupoService.findBy(nome, pageRequest);
		return ResponseEntity.ok().body(rs);
	}

}
