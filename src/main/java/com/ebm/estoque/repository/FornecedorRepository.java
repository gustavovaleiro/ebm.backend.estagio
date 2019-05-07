package com.ebm.estoque.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebm.estoque.domain.Fornecedor;

@Repository	
public interface FornecedorRepository  extends JpaRepository<Fornecedor, Integer>{

}
