package com.ebm.pessoal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.pessoal.domain.Cargo;
@Repository
public interface CargoRepository extends JpaRepository<Cargo, Integer> {
	
	@Transactional(readOnly = true)

	Page<Cargo> findByNomeCargoContainsIgnoreCase(String nomeCargo, Pageable pageRequest);
}
