package com.ebm.pessoal.dtos;

import java.io.Serializable;

import com.ebm.pessoal.domain.Cliente;
import com.ebm.pessoal.domain.TipoPessoa;

public class ClienteListDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String nome;
	private String tipo;
	private Double limteCompra;
	private String telefone;
	private String email;
	private String cidade;
	public ClienteListDTO() {}
	public ClienteListDTO(Integer id, String nome, TipoPessoa tipo, double limite, String telefone, String email, String cidade) {
		this.id=id;
		this.nome=nome;
		this.tipo = tipo.getDescricao();
		this.limteCompra = limite;
		this.telefone = telefone;
		this.email = email;
		this.cidade = cidade;
	
	}

	public ClienteListDTO(Cliente c) {
		this(c.getId(), c.getPessoa().getNome(), c.getPessoa().getTipo(), c.getLimite_compra().doubleValue(), c.getPessoa().getTelefonePrincipal().toString(), c.getPessoa().getEmailPrincipal().getEmail(), c.getPessoa().getEnderecoPrincipal().getCidade().toString());
	}
	public Double getLimteCompra() {
		return limteCompra;
	}
	public void setLimteCompra(Double limteCompra) {
		this.limteCompra = limteCompra;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	
}
