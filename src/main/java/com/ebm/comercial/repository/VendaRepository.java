package com.ebm.comercial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebm.comercial.domain.Venda;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Integer> {

}
