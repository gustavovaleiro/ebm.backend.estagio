package com.ebm.pessoal.resource;

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

import com.ebm.pessoal.domain.Funcionario;
import com.ebm.pessoal.domain.TipoPessoa;
import com.ebm.pessoal.dtos.FuncionarioListDTO;
import com.ebm.pessoal.service.FuncionarioService;

@RestController
@RequestMapping(value = "/funcionarios")
public class FuncionarioResource {
	@Autowired
	private FuncionarioService funcionarioService;

	@PreAuthorize("hasAuthority('FUNCIONARIO_POST')")
	@PostMapping
	public ResponseEntity<Void> insert(@RequestBody Funcionario funcionario) {
		Funcionario obj = funcionarioService.save(funcionario);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@PreAuthorize("hasAuthority('FUNCIONARIO_PUT')")
	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@RequestBody Funcionario funcionario, @PathVariable Integer id) {
		funcionario.setId(id);
		funcionario = funcionarioService.save(funcionario);
		return ResponseEntity.noContent().build();

	}

	@PreAuthorize("hasAuthority('FUNCIONARIO_DELETE')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		funcionarioService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAuthority('FUNCIONARIO_GET')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<Funcionario> find(@PathVariable Integer id) {
		Funcionario obj = funcionarioService.findById(id);
		return ResponseEntity.ok(obj);
	}

	@PreAuthorize("hasAuthority('FUNCIONARIO_GET')")
	@GetMapping(value = "/documents")
	public ResponseEntity<Funcionario> findBy(@RequestParam(value = "value", required = true) final String document) {

		return ResponseEntity.ok(funcionarioService.findByCpfOrCnpj(document));
	}

	@PreAuthorize("hasAuthority('FUNCIONARIO_GET')")
	@GetMapping(value = "/page")
	public ResponseEntity<Page<FuncionarioListDTO>> findAllBy(
			@RequestParam(value = "nome", required = false) String nome,
			@RequestParam(value = "tipo", required = false) String tipo,
			@RequestParam(value = "cargo", required = false) String cargo,
			@RequestParam(value = "matricula", required = false) String matricula,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "10") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {

		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Page<FuncionarioListDTO> rs = funcionarioService.findBy(TipoPessoa.fromString(tipo), cargo, nome, matricula,
				pageRequest);
		return ResponseEntity.ok().body(rs);
	}

}
