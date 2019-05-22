package com.ebm.auth.resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebm.auth.Modulo;
import com.ebm.auth.PermissaoE;


@RestController
@RequestMapping(value = "/permissoes")
public class PermissoesResource {
	
	@GetMapping(value = "/find")
	public ResponseEntity<Page<PermissaoE>> findAllPermissoesBy(
			@RequestParam(value ="desc", required = false) String desc,
			@RequestParam(value ="modulo_id", required = false) Integer modulo_id,
			@RequestParam(value ="modulo_name", required = false) Integer modulo_name,
			@RequestParam(value ="page", defaultValue="0") Integer page,
			@RequestParam(value ="linesPerPage", defaultValue="10")Integer linesPerPage,
			@RequestParam(value ="orderBy", defaultValue="nome")String orderBy,
			@RequestParam(value ="direction", defaultValue="ASC")String direction ) {
		Modulo modulo = null;
		if(modulo_id!=null) 
			modulo = Modulo.toEnum(modulo_id);
	    else if (modulo_name!=null) 
			 modulo = Modulo.toEnum(modulo_name);
		
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Page<PermissaoE> rs = PermissaoE.findPermissoesByDescAndModule(desc, modulo, pageRequest);
		return ResponseEntity.ok().body(rs);
	}
	
	
}
