package com.ebm.estoque.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ebm.estoque.domain.Item;

public interface ItemRepository extends JpaRepository<Item, Integer>{

	
}
