package com.ebm.estoque.domain;

import javax.persistence.DiscriminatorValue;

@DiscriminatorValue("S")
public class Servico extends Item{
	private static final long serialVersionUID = 1L;
	
}
