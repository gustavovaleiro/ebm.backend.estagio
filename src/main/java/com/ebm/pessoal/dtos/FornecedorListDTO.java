package com.ebm.pessoal.dtos;

import java.io.Serializable;

import com.ebm.pessoal.domain.Fornecedor;

public class FornecedorListDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String tipo;
	private String cpfCnpj;
	private String nome;
	private String telefone;
	private String email;
	private String cidade;

	public FornecedorListDTO() {
	}

	public FornecedorListDTO(Fornecedor fornecedor) {
		this(fornecedor.getId(),
				fornecedor.getPessoa().getTipo().getDescricao(),
				fornecedor.getPessoa().getDocument(),
				fornecedor.getPessoa().getNome(),
				fornecedor.getPessoa().getTelefone().iterator().next().toString(),
				fornecedor.getPessoa().getEmail().iterator().next().toString(),
				fornecedor.getPessoa().getEndereco().iterator().next().getCidade().getNome());
	}

	public FornecedorListDTO(Integer id, String tipo, String cpfCnpj, String nome, String telefone, String email,
			String cidade) {
		this.id = id;
		this.tipo = tipo;
		this.cpfCnpj = cpfCnpj;
		this.nome = nome;
		this.telefone = telefone;
		this.email = email;
		this.cidade = cidade;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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
