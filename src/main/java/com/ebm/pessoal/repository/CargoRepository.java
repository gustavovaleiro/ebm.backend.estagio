package com.ebm.pessoal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebm.pessoal.domain.Cargo;
@Repository
public interface CargoRepository extends JpaRepository<Cargo, Integer> {
	
	Optional<Cargo> findByNomeCargo(String nomeCargo);
}
