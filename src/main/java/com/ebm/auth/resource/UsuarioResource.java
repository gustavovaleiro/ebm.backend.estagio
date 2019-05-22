package com.ebm.auth.resource;

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

import com.ebm.auth.Usuario;
import com.ebm.auth.dto.UsuarioListDTO;
import com.ebm.auth.service.UsuarioService;


@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioResource {
	@Autowired
	private UsuarioService usuarioService;	
	
	@PostMapping
	public ResponseEntity<Void> insert( @RequestBody Usuario usuario){
		Usuario obj = usuarioService.save(usuario);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@RequestBody Usuario usuario, @PathVariable Integer id){
		usuario.setId(id);
		usuario = usuarioService.save(usuario);
		return ResponseEntity.noContent().build();
		
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		usuarioService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<Usuario> find(@PathVariable Integer id) {
		Usuario obj = usuarioService.find(id);
		return ResponseEntity.ok(obj);
	}
	
	@GetMapping
	public ResponseEntity<Usuario> findBy( 
			@RequestParam(value ="document", required = true) final String document){
		
		
		Usuario cli = usuarioService.findByCpfOrCnpj(document);
	
		
		return ResponseEntity.ok(cli);
	}

	@GetMapping(value="/page")
	public ResponseEntity<Page<UsuarioListDTO>> findAllBy(
			@RequestParam(value ="nome", required=false) String nome,
			@RequestParam(value ="grupo", required=false) Integer grupo,
			@RequestParam(value ="login", required=false) String login,
			@RequestParam(value ="email", required=false) String email,
			@RequestParam(value ="page", required=false) Integer page,
			@RequestParam(value ="linesPerPage", required=false)Integer linesPerPage,
			@RequestParam(value ="orderBy", required=false)String orderBy,
			@RequestParam(value ="direction", required=false)String direction ) {
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Page<UsuarioListDTO> rs = usuarioService.findBy(nome, grupo, login, email, pageRequest);
		return ResponseEntity.ok().body(rs);
	}
	
	
}
