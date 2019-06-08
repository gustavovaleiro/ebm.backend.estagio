package com.ebm.pessoal.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import com.ebm.security.Usuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoCadastral implements Serializable {
	private static final long serialVersionUID = 1L;
	 @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime  dataCadastro;
	@ManyToOne
	@JsonIgnore
	private Usuario usuarioCadastro;

	 @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dataUltimaModificacao;
	@ManyToOne
	@JsonIgnore
	private Usuario ultimaModificacao;

	public String getNomeUsuarioCadastro() {
		if(usuarioCadastro!=null && usuarioCadastro.getFuncionario() != null)
			return usuarioCadastro.getFuncionario().getPessoa().getNome();
		return null;
	}
	public String getNomeUsuarioUltimaModificacao() {
		if(ultimaModificacao!=null && ultimaModificacao.getFuncionario() != null)
			return ultimaModificacao.getFuncionario().getPessoa().getNome();
		return null;
	}
	
}
