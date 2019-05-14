package com.ebm.estoque.dtos;

import java.io.Serializable;

import com.ebm.Utils;
import com.ebm.estoque.domain.Movimentacao;

public class MovimentacaoListDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String tipo;
	private String data;
	private String documento;
	
	public MovimentacaoListDTO() {}
	
	public MovimentacaoListDTO(Integer id, String tipo, String data, String documento) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.data = data;
		this.documento = documento;
	}
	public MovimentacaoListDTO(Movimentacao movimentacao) {
		this(movimentacao.getId(), movimentacao.getTipoMovimentacao().getDesc(), movimentacao.getDataMovimentacao().format(Utils.getBrDateTimeFormatter()), movimentacao.getDocumento());
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

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}
}
