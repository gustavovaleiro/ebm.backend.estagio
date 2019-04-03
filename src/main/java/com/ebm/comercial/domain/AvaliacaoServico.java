package com.ebm.comercial.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.OptionalDouble;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.ebm.auth.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
public class AvaliacaoServico  implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@ManyToOne
	private Usuario entrada;
	@JsonIgnore
	@OneToOne(mappedBy="avaliacao")
	private OrdemServico servico;
	@Column(length = 400)
	private String comentario;
	@OneToMany
	private Set<CriterioAvaliado> avaliacoes = new HashSet<CriterioAvaliado>();
	
	private LocalDateTime  dataCadastro = LocalDateTime.now();
	@ManyToOne
	private Usuario usuarioCadastro;
	private LocalDateTime dataUltimaModificacao;
	@ManyToOne
	private Usuario ultimaModificacao;
	

	public AvaliacaoServico() {}
	public AvaliacaoServico(Integer id, Usuario entrada, OrdemServico servico, String comentario) {
		super();
		this.id = id;
		this.entrada = entrada;
		this.servico = servico;
		this.comentario = comentario;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Usuario getEntrada() {
		return entrada;
	}
	public void setEntrada(Usuario entrada) {
		this.entrada = entrada;
	}
	public OrdemServico getServico() {
		return servico;
	}
	public void setServico(OrdemServico servico) {
		this.servico = servico;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public Set<CriterioAvaliado> getAvaliacoes() {
		return avaliacoes;
	}
	public void setAvaliacoes(Set<CriterioAvaliado> avaliacoes) {
		this.avaliacoes = avaliacoes;
	}
	public double getMediaAvaliacao() {
		
		OptionalDouble average = avaliacoes.stream().mapToDouble(x -> x.getNota()).average();
		return average.orElse(0d);
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
		AvaliacaoServico other = (AvaliacaoServico) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	

}
