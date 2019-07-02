package com.ebm.security.resource;

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

import com.ebm.security.Usuario;
import com.ebm.security.dto.UsuarioListDTO;
import com.ebm.security.dto.UsuarioNewDTO;
import com.ebm.security.dto.UsuarioUpdateDTO;
import com.ebm.security.service.UsuarioService;

@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioResource {
	@Autowired
	private UsuarioService usuarioService;

	@PreAuthorize("hasAuthority('USUARIO_POST')")
	@PostMapping
	public ResponseEntity<Void> insert(@Valid @RequestBody UsuarioNewDTO usuario) {
		Usuario obj = usuarioService.save(usuario);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@PreAuthorize("hasAuthority('USUARIO_PUT')")
	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@Valid @RequestBody UsuarioUpdateDTO usuario, @PathVariable Integer id) {
		usuario.setId(id);
	    usuarioService.update(usuario);
		return ResponseEntity.noContent().build();

	}

	@PreAuthorize("hasAuthority('USUARIO_DELETE')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		usuarioService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAuthority('USUARIO_GET')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<Usuario> find(@PathVariable Integer id) {
		Usuario obj = usuarioService.find(id);
		return ResponseEntity.ok(obj);
	}

	@PreAuthorize("hasAuthority('USUARIO_GET')")
	@GetMapping(value ="/document")
	public ResponseEntity<Usuario> findBy(@RequestParam(value = "value", required = true) final String document) {

		Usuario cli = usuarioService.findByCpfOrCnpj(document);

		return ResponseEntity.ok(cli);
	}

	@PreAuthorize("hasAuthority('USUARIO_GET')")
	@GetMapping(value = "/page")
	public ResponseEntity<Page<UsuarioListDTO>> findAllBy(@RequestParam(value = "nome", required = false) String nome,
			@RequestParam(value = "login", required = false) String login,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "10") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {

		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Page<UsuarioListDTO> rs = usuarioService.findBy(nome, login, email, pageRequest);
		return ResponseEntity.ok().body(rs);
	}

}
