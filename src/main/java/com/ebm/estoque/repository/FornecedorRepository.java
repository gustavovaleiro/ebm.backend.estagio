package com.ebm.estoque.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.estoque.domain.CategoriaItem;
import com.ebm.estoque.domain.Fornecedor;

@Repository	
public interface FornecedorRepository  extends JpaRepository<Fornecedor, Integer>{
	
	@Transactional(readOnly=true)
	List<Fornecedor> findByCategoriasIn(Set<CategoriaItem> findAllById);

}
