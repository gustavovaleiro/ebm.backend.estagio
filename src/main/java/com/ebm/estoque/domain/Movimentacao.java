package com.ebm.estoque.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.ebm.auth.Usuario;

@Entity
public abstract class Movimentacao implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 20)
	private String documento;
	@Column(length = 400)
	private String descricao;
	
	private LocalDate dataMovimentacao;
	
	private LocalDateTime  dataCadastro;
	private Usuario usuarioCadastro;
	private LocalDateTime dataUltimaModificacao;
	private Usuario ultimaModificacao;
	
	public Movimentacao() {
		this.dataCadastro =  LocalDateTime.now();
	}
	
	public Movimentacao(Integer id, String documento, String descricao, LocalDate dataMovimentacao) {
		super();
		this.id = id;
		this.documento = documento;
		this.descricao = descricao;
		this.dataMovimentacao = dataMovimentacao;
	}

	public Integer getId() {
		return id;
	}
	public LocalDateTime getDataUltimaModificacao() {
		return dataUltimaModificacao;
	}

	public void setDataUltimaModificacao(LocalDateTime dataUltimaModificacao) {
		this.dataUltimaModificacao = dataUltimaModificacao;
	}

	public Usuario getUltimaModificacao() {
		return ultimaModificacao;
	}

	public void setUltimaModificacao(Usuario ultimaModificacao) {
		this.ultimaModificacao = ultimaModificacao;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDate getDataMovimentacao() {
		return dataMovimentacao;
	}

	public void setDataMovimentacao(LocalDate dataMovimentacao) {
		this.dataMovimentacao = dataMovimentacao;
	}

	public LocalDateTime getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(LocalDateTime dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Usuario getUsuarioCadastro() {
		return usuarioCadastro;
	}

	public void setUsuarioCadastro(Usuario usuarioCadastro) {
		this.usuarioCadastro = usuarioCadastro;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Movimentacao other = (Movimentacao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
