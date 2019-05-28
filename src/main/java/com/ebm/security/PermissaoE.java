package com.ebm.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.fasterxml.jackson.annotation.JsonIgnore;

public enum PermissaoE  {
	
	
	FUNCIONARIO_GET(0, Modulo.PESSOAL ,"FUNCIONARIO_GET", "Vizualização de funcionarios já cadastrados."),
	FUNCIONARIO_PUT(1, Modulo.PESSOAL ,"FUNCIONARIO_PUT", "Atualização de funcionarios já cadastrados."),
	FUNCIONARIO_POST(2, Modulo.PESSOAL ,"FUNCIONARIO_POST", "Cadastro de novos funcionarios."),
	FUNCIONARIO_DELETE(3, Modulo.PESSOAL ,"FUNCIONARIO_DELETE", "Deletar funcionarios."),
	
	CARGO_GET(10, Modulo.PESSOAL, "CARGO_GET","Vizualização de cargos já cadastrados." ),
	CARGO_PUT(11, Modulo.PESSOAL, "CARGO_PUT","Atualização de cargos já cadastrados." ),
	CARGO_POST(12, Modulo.PESSOAL, "CARGO_POST","Cadastro de novos cargos."),
	CARGO_DELETE(13, Modulo.PESSOAL, "CARGO_DELETE","Deletar cargos."),
	
	CLIENTE_GET(20, Modulo.PESSOAL,"CLIENTE_GET", "Vizualização de clientes já cadastrados." ),
	CLIENTE_PUT(21, Modulo.PESSOAL,"CLIENTE_PUT", "Atualização de clientes já cadastrados." ),
	CLIENTE_POST(22, Modulo.PESSOAL,"CLIENTE_POST", "Cadastro de clientes cargos."),
	CLIENTE_DELETE(23, Modulo.PESSOAL,"CLIENTE_DELETE", "Deletar clientes."),
	
	FORNECEDOR_GET(30, Modulo.PESSOAL, "FORNECEDOR_GET","Vizualização de entradas/saidas já cadastrados." ),
	FORNECEDOR_PUT(31, Modulo.PESSOAL, "FORNECEDOR_PUT", "Atualização de  entradas/saidas  já cadastrados." ),
	FORNECEDOR_POST(32, Modulo.PESSOAL, "FORNECEDOR_POST", "Cadastro de entradas/saidas."),
	FORNECEDOR_DELETE(33, Modulo.PESSOAL, "FORNECEDOR_DELETE", "Deletar entradas/saidas."),
	
	
	ITEM_GET(100, Modulo.ESTOQUE,"ITEM_GET",  "Vizualização de produtos e serviços já cadastrados." ),
	ITEM_PUT(101, Modulo.ESTOQUE,"ITEM_PUT", "Atualização de  produtos e serviços já cadastrados." ),
	ITEM_POST(102, Modulo.ESTOQUE,"ITEM_POST", "Cadastro de  produtos e serviços."),
	ITEM_DELETE(103, Modulo.ESTOQUE,"ITEM_DELETE", "Deletar  produtos e serviços."),
	
	ITEM_AUX_GET(110, Modulo.ESTOQUE, "ITEM_AUX_GET", "Vizualização de unidades e categorias já cadastrados." ),
	ITEM_AUX_PUT(111, Modulo.ESTOQUE,  "ITEM_AUX_PUT", "Atualização de  unidades e categorias já cadastrados." ),
	ITEM_AUX_POST(112, Modulo.ESTOQUE,  "ITEM_AUX_POST", "Cadastro de unidades e categorias."),
	ITEM_AUX_DELETE(113, Modulo.ESTOQUE,  "ITEM_AUX_DELETE", "Deletar unidades e categorias"),
	
	MOVIMENTACAO_GET(120, Modulo.ESTOQUE, "MOVIMENTACAO_GET","Vizualização de entradas/saidas já cadastrados." ),
	MOVIMENTACAO_PUT(121, Modulo.ESTOQUE, "MOVIMENTACAO_PUT", "Atualização de  entradas/saidas  já cadastrados." ),
	MOVIMENTACAO_POST(122, Modulo.ESTOQUE, "MOVIMENTACAO_POST", "Cadastro de entradas/saidas."),
	MOVIMENTACAO_DELETE(123, Modulo.ESTOQUE, "MOVIMENTACAO_DELETE", "Deletar entradas/saidas."),
	
	
	USUARIO_GET(200, Modulo.AUTH, "USUARIO_GET", "Vizualização de usuarios já cadastrados." ),
	USUARIO_PUT(201, Modulo.AUTH, "USUARIO_PUT", "Atualização de  usuarios  já cadastrados." ),
	USUARIO_POST(202, Modulo.AUTH, "USUARIO_POST", "Cadastro de usuarios."),
	USUARIO_DELETE(203, Modulo.AUTH, "USUARIO_DELETE",  "Deletar usuario."),
	
	GRUPO_PERMISSAO_GET(210, Modulo.AUTH, "GRUPO_PERMISSAO_GET", "Vizualização de grupos de usuarios já cadastrados." ),
	GRUPO_PERMISSAO_PUT(211, Modulo.AUTH, "GRUPO_PERMISSAO_PUT", "Atualização de grupos de usuarios já cadastrados." ),
	GRUPO_PERMISSAO_POST(212, Modulo.AUTH, "GRUPO_PERMISSAO_POST","Cadastro de grupos de usuarios."),
	GRUPO_PERMISSAO_DELETE(213, Modulo.AUTH, "GRUPO_PERMISSAO_DELETE", "Deletar grupos de usuarios.");
	
	
	
	private Integer id;
	
	private Modulo mod;
	@JsonIgnore
	private String nome;
	private String descricao;

	private PermissaoE(Integer id, Modulo mod, String nome, String descricao) {
		this.id =id;
		this.mod=mod;
		this.nome =nome;
		this.descricao = descricao;
	}

	public Integer getId() {
		return id;
	}

	public Modulo getMod() {
		return mod;
	}

	public String getNome() {
		return nome;
	}

	public String getDescricao() {
		return descricao;
	}
	public static PermissaoE toEnum(Integer id) {
		if(id == null) {
			return null;
		}
		for(PermissaoE p: PermissaoE.values()) {
			if(id.equals(p.getId()))
				return p;
		}
		
		throw new IllegalArgumentException("id invalido: " + id);
	}
	
	public static Page<PermissaoE> findPermissoesByDescAndModule(String desc, Modulo modulo, PageRequest page){

		
		List<PermissaoE> permissoes = new ArrayList<>(Arrays.asList(PermissaoE.values()));
		if(desc!=null) {
			permissoes = permissoes.stream().filter( p -> p.getDescricao().toLowerCase().contains(desc.toLowerCase())).collect(Collectors.toList());
		}
		if(modulo!=null) {
			permissoes = permissoes.stream().filter( p -> p.getMod().equals(modulo)).collect(Collectors.toList());
		}
		
		return new PageImpl<>(permissoes, page, permissoes.size());
	}


	
	
}
