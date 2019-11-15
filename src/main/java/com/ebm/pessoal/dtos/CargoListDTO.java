package com.ebm.pessoal.dtos;

import com.ebm.pessoal.domain.Cargo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CargoListDTO {
	
	private Integer id;
	private String nome;
	
	public CargoListDTO(Cargo cargo) {
		this(cargo.getId(), cargo.getNomeCargo());
	}

}
