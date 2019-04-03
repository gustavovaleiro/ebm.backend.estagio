package com.ebm.estoque.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
@Entity
@DiscriminatorValue("S")
public class Servico extends Item{
	private static final long serialVersionUID = 1L;
	
}
