package com.ebm.auth;

public enum Modulo {
	PESSOAL(1,"Pessoal", "Cadastro de pessoal, clientes, colaboradores, usuarios e permissoes."),
	ESTOQUE(2,"Estoque", "Cadastro de estoque, produtos e serviços, entradas e saidas e as movimentações");
	
	private int id;
	private String nome;
	private String desc;

	Modulo(int id, String nome, String desc) {
		this.id = id;
		this.nome = nome;
		this.desc = desc;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static Modulo toEnum(Integer modulo) {
		if(modulo == null) {
			return null;
		}
		for(Modulo x: Modulo.values()) {
			if(modulo.equals(x.getId()))
				return x;
		}
		
		throw new IllegalArgumentException("id invalido: " + modulo);
		
	}
	
	
	
}
