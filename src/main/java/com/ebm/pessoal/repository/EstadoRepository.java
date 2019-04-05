package com.ebm.pessoal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ebm.pessoal.domain.Estado;

public interface EstadoRepository extends JpaRepository<Estado, Integer> {

	Optional<Estado> findOneByUF(String uf);


}
