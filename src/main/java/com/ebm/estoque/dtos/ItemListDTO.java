package com.ebm.estoque.dtos;

import java.io.Serializable;

import com.ebm.estoque.domain.Item;



public class ItemListDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String codInterno;
	private String tipo;
	private String nome;
	private String descricao;
	private String unidade;
	private String categoria;
	
	public ItemListDTO() {}
	public ItemListDTO(Integer id, String codInterno, String tipo, String nome, String descricao, String unidade,
			String categoria) {
		super();
		this.id = id;
		this.codInterno = codInterno;
		this.tipo = tipo;
		this.nome = nome;
		this.descricao = descricao;
		this.unidade = unidade;
		this.categoria = categoria;
	}
	public ItemListDTO(Item item) {
		
		this(item.getId(), item.getCodInterno(), item.getTipo(), item.getNome(), item.getDescricao(), item.getUnidade().getAbrev(), item.getCategoria().getNome());
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCodInterno() {
		return codInterno;
	}
	public void setCodInterno(String codInterno) {
		this.codInterno = codInterno;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getUnidade() {
		return unidade;
	}
	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	
	
}
