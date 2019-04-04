package com.ebm.auth;

public enum Modulo {
	PESSOAL(1, "Cadastro de pessoal, clientes, colaboradores, usuarios e permissoes."),
	ESTOQUE(2, "Cadastro de estoque, produtos e serviços, entradas e saidas e as movimentações");
	
	private int id;
	private String desc;

	Modulo(int id, String desc) {
		this.id = id;
		this.desc = desc;
	}
	
	
}
