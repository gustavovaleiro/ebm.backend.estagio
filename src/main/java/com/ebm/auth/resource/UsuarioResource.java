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
import com.ebm.exceptions.DataIntegrityException;


@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioResource {
	@Autowired
	private UsuarioService usuarioService;	
	
	@PostMapping
	public ResponseEntity<Void> insert( @RequestBody Usuario usuario){
		Usuario obj = usuarioService.insert(usuario);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@RequestBody Usuario usuario, @PathVariable Integer id){
		usuario.setId(id);
		usuario = usuarioService.update(usuario);
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
			@RequestParam(value ="cpf", defaultValue="", required = false) final String cpf,
			@RequestParam(value = "cnpj", defaultValue="", required = false) final String cnpj){
		
		Usuario cli;
		if(!cpf.equals("") || !cnpj.equals("")) 
			cli = usuarioService.findByCpfOrCnpj(cpf, cnpj);
		 else 
			throw new DataIntegrityException("Não foi passado dados");
		
		return ResponseEntity.ok(cli);
	}

	@GetMapping(value="/page")
	public ResponseEntity<Page<UsuarioListDTO>> findAllBy(
			@RequestParam(value ="nome", defaultValue="") String nome,
			@RequestParam(value ="grupo", defaultValue="-1") Integer grupo,
			@RequestParam(value ="login", defaultValue="") String login,
			@RequestParam(value ="email", defaultValue="") String email,
			@RequestParam(value ="page", defaultValue="0") Integer page,
			@RequestParam(value ="linesPerPage", defaultValue="10")Integer linesPerPage,
			@RequestParam(value ="orderBy", defaultValue="nome")String orderBy,
			@RequestParam(value ="direction", defaultValue="ASC")String direction ) {
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Page<UsuarioListDTO> rs = usuarioService.findBy(nome, grupo, login, email, pageRequest);
		return ResponseEntity.ok().body(rs);
	}
	
	
}
