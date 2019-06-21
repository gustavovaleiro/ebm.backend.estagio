package com.ebm.estoque.resource;

import java.net.URI;
import java.util.List;

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

import com.ebm.estoque.domain.Movimentacao;
import com.ebm.estoque.domain.enums.TipoMovimentacao;
import com.ebm.estoque.dtos.MovimentacaoListDTO;
import com.ebm.estoque.service.interfaces.MovimentacaoService;

@RestController
@RequestMapping(value = "/movimentacoes")
public class MovimentacaoResource {
	@Autowired
	private MovimentacaoService movimentacaoService;

	@PreAuthorize("hasAuthority('MOVIMENTACAO_POST')")
	@PostMapping
	public ResponseEntity<Void> insert(@Valid @RequestBody Movimentacao movimentacao) {
		Movimentacao obj = movimentacaoService.save(movimentacao);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@PreAuthorize("hasAuthority('MOVIMENTACAO_PUT')")
	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@Valid @RequestBody Movimentacao movimentacao, @PathVariable Integer id) {
		movimentacao.setId(id);
		movimentacao = movimentacaoService.save(movimentacao);
		return ResponseEntity.noContent().build();

	}

	@PreAuthorize("hasAuthority('MOVIMENTACAO_DELETE')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		movimentacaoService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAuthority('MOVIMENTACAO_GET')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<Movimentacao> find(@PathVariable Integer id) {
		Movimentacao obj = movimentacaoService.findById(id);
		return ResponseEntity.ok(obj);
	}

	@PreAuthorize("hasAuthority('MOVIMENTACAO_GET')")
	@GetMapping(value = "/page")
	public ResponseEntity<Page<MovimentacaoListDTO>> findAllBy(
			@RequestParam(value = "documento", required = false) String documento,
			@RequestParam(value = "tipo", required = false) String tipo,
			@RequestParam(value = "fornecedores", required = false) List<Integer> fornecedores,
			@RequestParam(value = "produtos", required = false) List<Integer> produtos,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "10") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {

		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Page<MovimentacaoListDTO> rs = movimentacaoService.findBy(TipoMovimentacao.fromString(tipo), documento,
				fornecedores, produtos, pageRequest);
		return ResponseEntity.ok().body(rs);
	}

}
