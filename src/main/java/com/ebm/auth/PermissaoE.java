package com.ebm.auth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

public enum PermissaoE {
	
	
	FUNCIONARIO_GET(1, Modulo.PESSOAL ,"Funcionario", "Vizualização de funcionarios já cadastrados."),
	FUNCIONARIO_PUT(2, Modulo.PESSOAL ,"Funcionario", "Atualização de funcionarios já cadastrados."),
	FUNCIONARIO_POST(3, Modulo.PESSOAL ,"Funcionario", "Cadastro de novos funcionarios."),
	FUNCIONARIO_DELETE(4, Modulo.PESSOAL ,"Funcionario", "Deletar funcionarios."),
	
	CARGO_GET(5, Modulo.PESSOAL, "Cargo","Vizualização de cargos já cadastrados." ),
	CARGO_PUT(6, Modulo.PESSOAL, "Cargo","Atualização de cargos já cadastrados." ),
	CARGO_POST(7, Modulo.PESSOAL, "Cargo","Cadastro de novos cargos."),
	CARGO_DELETE(8, Modulo.PESSOAL, "Cargo","Deletar cargos."),
	
	CLIENTE_GET(9, Modulo.PESSOAL,"Cliente", "Vizualização de clientes já cadastrados." ),
	CLIENTE_PUT(10, Modulo.PESSOAL,"Cliente", "Atualização de clientes já cadastrados." ),
	CLIENTE_POST(11, Modulo.PESSOAL,"Cliente", "Cadastro de clientes cargos."),
	CLIENTE_DELETE(12, Modulo.PESSOAL,"Cliente", "Deletar clientes."),
	
	
	
	ITEM_GET(13, Modulo.ESTOQUE,"Item",  "Vizualização de produtos e serviços já cadastrados." ),
	ITEM_PUT(14, Modulo.ESTOQUE,"Item", "Atualização de  produtos e serviços já cadastrados." ),
	ITEM_POST(15, Modulo.ESTOQUE,"Item", "Cadastro de  produtos e serviços."),
	ITEM_DELETE(16, Modulo.ESTOQUE,"Item", "Deletar  produtos e serviços."),
	
	ITEM_AUX_GET(17, Modulo.ESTOQUE, "Auxiliares Item", "Vizualização de unidades e categorias já cadastrados." ),
	ITEM_AUX_PUT(18, Modulo.ESTOQUE,  "Auxiliares Item", "Atualização de  unidades e categorias já cadastrados." ),
	ITEM_AUX_POST(19, Modulo.ESTOQUE,  "Auxiliares Item", "Cadastro de unidades e categorias."),
	ITEM_AUX_DELETE(20, Modulo.ESTOQUE,  "Auxiliares Item", "Deletar unidades e categorias"),
	
	MOVIMENTACAO_GET(21, Modulo.ESTOQUE, "Movimentação","Vizualização de entradas/saidas já cadastrados." ),
	MOVIMENTACAO_PUT(22, Modulo.ESTOQUE, "Movimentação", "Atualização de  entradas/saidas  já cadastrados." ),
	MOVIMENTACAO_POST(23, Modulo.ESTOQUE, "Movimentação", "Cadastro de entradas/saidas "),
	MOVIMENTACAO_DELETE(24, Modulo.ESTOQUE, "Movimentação", "Deletar entradas/saidas "),
	
	USUARIO_GET(25, Modulo.AUTH, "Usuario", "Vizualização de entradas/saidas já cadastrados." ),
	USUARIO_PUT(26, Modulo.AUTH, "Usuario", "Atualização de  entradas/saidas  já cadastrados." ),
	USUARIO_POST(27, Modulo.AUTH, "Usuario", "Cadastro de entradas/saidas "),
	USUARIO_DELETE(28, Modulo.AUTH, "Usuario",  "Deletar entradas/saidas "),
	GRUPO_PERMISSAO_GET(29, Modulo.AUTH, "Grupos de usuarios", "Compreende o cadastro/vizualização de grupos de usuarios, bem como suas permissões"),
	GRUPO_PERMISSAO_PUT(31, Modulo.AUTH, "Grupos de usuarios", "Compreende o cadastro/vizualização de grupos de usuarios, bem como suas permissões"),
	GRUPO_PERMISSAO_POST(32, Modulo.AUTH, "Grupos de usuarios", "Compreende o cadastro/vizualização de grupos de usuarios, bem como suas permissões"),
	GRUPO_PERMISSAO_DELETE(33, Modulo.AUTH, "Grupos de usuarios", "Compreende o cadastro/vizualização de grupos de usuarios, bem como suas permissões");
	
	
	
	private Integer id;
	private Modulo mod;
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
