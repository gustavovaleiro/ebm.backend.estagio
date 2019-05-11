package com.ebm.estoque.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.ebm.auth.Usuario;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name  = "_tipo", discriminatorType= DiscriminatorType.STRING, length=1)
public abstract class Item implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false, length = 140)
	private String nome;
	
	@Column(nullable = false, length = 400)
	private String descricao;
	
	@ManyToOne()
	@JoinColumn(nullable = false)
	private Unidade unidade;
	
	@ManyToOne()
	@JoinColumn(nullable = false)
	private CategoriaItem categoria;
	
	@Column(length = 60)
	private String codInterno;
	
	private BigDecimal valorCompraMedio;
	private BigDecimal outrasDespesa;
	private Double margemLucro;
	private Double comissaoVenda;
	private LocalDateTime  dataCadastro;
	@ManyToOne
	private Usuario usuarioCadastro;
	private LocalDateTime dataUltimaModificacao;
	@ManyToOne
	private Usuario ultimaModificacao;
	protected String tipo;
	
	public Item() {}


	
	public Item(Integer id, String nome, String descricao, Unidade unidade, CategoriaItem categoria, String codInterno,
			BigDecimal valorCompraMedio, BigDecimal outrasDespesa, Double margemLucro, Double comissaoVenda) {
		super();
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.unidade = unidade;
		this.categoria = categoria;
		this.codInterno = codInterno;
		this.valorCompraMedio = valorCompraMedio;
		this.outrasDespesa = outrasDespesa;
		this.margemLucro = margemLucro;
		this.comissaoVenda = comissaoVenda;

	}
	

	public String getTipo() {
		return tipo;
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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	public CategoriaItem getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaItem categoria) {
		this.categoria = categoria;
	}

	public String getCodInterno() {
		return codInterno;
	}

	public void setCodInterno(String codInterno) {
		this.codInterno = codInterno;
	}

	public BigDecimal getValorCompraMedio() {
		return valorCompraMedio;
	}

	public void setValorCompraMedio(BigDecimal valorCompraMedio) {
		this.valorCompraMedio = valorCompraMedio;
	}

	public BigDecimal getOutrasDespesa() {
		return outrasDespesa;
	}

	public void setOutrasDespesa(BigDecimal outrasDespesa) {
		this.outrasDespesa = outrasDespesa;
	}

	public Double getMargemLucro() {
		return margemLucro;
	}

	public void setMargemLucro(Double margemLucro) {
		this.margemLucro = margemLucro;
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
	@Transient
	public BigDecimal getPrecoVenda() {
		return (getCustoTotal()).multiply(BigDecimal.valueOf(this.margemLucro + 1d));
	}
	@Transient
	public BigDecimal getLucroEstimado() {
		return getCustoTotal().multiply(BigDecimal.valueOf(this.margemLucro));
	}
	@Transient
	public BigDecimal getComissaoEstimada() {
		return  getPrecoVenda().multiply(BigDecimal.valueOf(comissaoVenda));
	}
	@Transient
	public Double getComissaoVenda() {
		return comissaoVenda;
	}

	public void setComissaoVenda(Double comissaoVenda) {
		this.comissaoVenda = comissaoVenda;
	}
	@Transient
	public BigDecimal getCustoTotal() {
		return this.valorCompraMedio.add(Optional.ofNullable(this.outrasDespesa).orElse(BigDecimal.valueOf(0)));
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
		Item other = (Item) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
