package com.ebm.estoque.resource;

import java.net.URI;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ebm.estoque.domain.Unidade;
import com.ebm.estoque.service.interfaces.UnidadeService;

@RestController
@RequestMapping(value = "/unidades")
public class UnidadeResource {
	@Autowired
	private UnidadeService unidadeService;
	@PreAuthorize("hasAuthority('ITEM_AUX_POST')")
	@PostMapping
	public ResponseEntity<Unidade> insert(@RequestBody Unidade unidade) {
		Unidade unidadeS = unidadeService.save(unidade);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(unidadeS.getId())
				.toUri();
		return ResponseEntity.created(uri).body(unidadeS);
	}
	@PreAuthorize("hasAuthority('ITEM_AUX_PUT')")
	@PutMapping(value = "/{id}")
	public ResponseEntity<Unidade> update(@RequestBody Unidade unidade, @PathVariable Integer id) {
		unidade.setId(id);
		unidade = unidadeService.save(unidade);
		return ResponseEntity.ok(unidade);

	}
	@PreAuthorize("hasAuthority('ITEM_AUX_DELETE')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		unidadeService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAuthority('ITEM_AUX_GET')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<Unidade> find(@PathVariable Integer id) {
		Unidade obj = unidadeService.findById(id);
		return ResponseEntity.ok(obj);
	}

	@PreAuthorize("hasAuthority('ITEM_AUX_GET')")
	@GetMapping(value = "/find")
	public ResponseEntity<Unidade> findAllBy(@RequestParam(value = "nome", required = false) String abrev) {

		Unidade rs = unidadeService.findByAbrev(abrev);
		return ResponseEntity.ok().body(rs);
	}

}
