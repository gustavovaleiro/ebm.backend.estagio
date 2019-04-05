package com.ebm.auth.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebm.auth.Permissao;
import com.ebm.auth.service.GrupoPermissaoService;


@RestController
@RequestMapping(value = "/permissoes")
public class PermissoesResource {
	@Autowired
	private GrupoPermissaoService permissoesService;	
	
	@GetMapping(value="")
	public ResponseEntity<Page<Permissao>> findAllPermissoesBy(
			@RequestParam(value ="modulo", defaultValue="-1") Integer modulo_id,
			@RequestParam(value ="page", defaultValue="0") Integer page,
			@RequestParam(value ="linesPerPage", defaultValue="10")Integer linesPerPage,
			@RequestParam(value ="orderBy", defaultValue="nome")String orderBy,
			@RequestParam(value ="direction", defaultValue="ASC")String direction ) {
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Page<Permissao> rs = permissoesService.findPermissoesByModulo(modulo_id, pageRequest);
		return ResponseEntity.ok().body(rs);
	}
	
	
}
