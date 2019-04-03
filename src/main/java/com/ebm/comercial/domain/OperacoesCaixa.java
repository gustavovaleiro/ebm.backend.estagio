package com.ebm.comercial.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.ebm.auth.Usuario;
@Entity
public class OperacoesCaixa implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TipoOperacao tipo;
	@ManyToOne
	private Usuario usuarioAutorizou;
	@Column(nullable = false)
	private LocalDateTime momento = LocalDateTime.now();
	@Column(nullable = false)
	private BigDecimal quantia;
	
	public OperacoesCaixa() {}
	
	
	public OperacoesCaixa( TipoOperacao tipo, Usuario usuarioAutorizou,
			BigDecimal quantia) {
		super();
		this.tipo = tipo;
		this.usuarioAutorizou = usuarioAutorizou;
		this.quantia = quantia;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TipoOperacao getTipo() {
		return tipo;
	}

	public void setTipo(TipoOperacao tipo) {
		this.tipo = tipo;
	}

	public Usuario getUsuarioAutorizou() {
		return usuarioAutorizou;
	}

	public void setUsuarioAutorizou(Usuario usuarioAutorizou) {
		this.usuarioAutorizou = usuarioAutorizou;
	}

	public LocalDateTime getMomento() {
		return momento;
	}

	public BigDecimal getQuantia() {
		return quantia;
	}

	public void setQuantia(BigDecimal quantia) {
		this.quantia = quantia;
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
		OperacoesCaixa other = (OperacoesCaixa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
