package com.ebm.comercial.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;

import com.ebm.auth.Usuario;

@Entity
public class Caixa implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(nullable = false, length = 60)
	private String codigoCaixa;
	@ColumnDefault("0")
	private BigDecimal valorAbertura;
	@Column(nullable = false)
	private BigDecimal valorFechamento;
	private LocalDateTime fechamento;
	@Column(length = 60)
	private String planoContas;
	@OneToMany
	private List<OperacoesCaixa> operacoes;
	@ManyToOne
	private Usuario usuario_fechou;
	
	@OneToMany
	private Set<VendaAberta> vendas = new HashSet<VendaAberta>();
	
	private LocalDateTime  abertura = LocalDateTime.now();
	@ManyToOne
	private Usuario usuarioCadastro;


	private boolean fechado;
	public Caixa() {}

	public Caixa(Integer id, String codigoCaixa, BigDecimal valorAbertura, LocalDateTime abertura,
			BigDecimal valorFechamento, LocalDateTime fechamento, String planoContas) {
		super();
		this.id = id;
		this.codigoCaixa = codigoCaixa;
		this.valorAbertura = valorAbertura;
		this.abertura = abertura;
		this.valorFechamento = valorFechamento;
		this.fechamento = fechamento;
		this.planoContas = planoContas;
	}

	public Integer getId() {
		return id;
	}
	public void sangria(Usuario user, BigDecimal quantia) {
		operacoes.add(new OperacoesCaixa( TipoOperacao.SANGRIA, user, quantia));
	}
	public void reforco(Usuario user, BigDecimal quantia) {
		operacoes.add(new OperacoesCaixa( TipoOperacao.REFORCO, user, quantia));
	}
	public void fechaCaixa(Usuario user) {
		fechado = true;
		fechamento = LocalDateTime.now();
		this.usuario_fechou = user;
		double valorOperacoes = operacoes.stream().mapToDouble( x-> x.getTipo() == TipoOperacao.REFORCO ?  
				x.getQuantia().doubleValue() : x.getQuantia().multiply(BigDecimal.valueOf(-1)).doubleValue()).sum();
		double totalVendas = vendas.stream().mapToDouble( x-> x.getValorTotal()).sum();
	
		
		this.valorFechamento = valorAbertura.add( BigDecimal.valueOf(valorOperacoes )).add(BigDecimal.valueOf(totalVendas));
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getCodigoCaixa() {
		return codigoCaixa;
	}

	public void setCodigoCaixa(String codigoCaixa) {
		this.codigoCaixa = codigoCaixa;
	}

	public BigDecimal getValorAbertura() {
		return valorAbertura;
	}

	public void setValorAbertura(BigDecimal valorAbertura) {
		this.valorAbertura = valorAbertura;
	}

	public LocalDateTime getAbertura() {
		return abertura;
	}

	public void setAbertura(LocalDateTime abertura) {
		this.abertura = abertura;
	}

	public BigDecimal getValorFechamento() {
		return valorFechamento;
	}

	public void setValorFechamento(BigDecimal valorFechamento) {
		this.valorFechamento = valorFechamento;
	}
	
	

	public boolean isFechado() {
		return fechado;
	}

	public LocalDateTime getFechamento() {
		return fechamento;
	}

	public void setFechamento(LocalDateTime fechamento) {
		this.fechamento = fechamento;
	}

	public String getPlanoContas() {
		return planoContas;
	}

	public void setPlanoContas(String planoContas) {
		this.planoContas = planoContas;
	}
	
	public Set<VendaAberta> getVendas() {
		return vendas;
	}

	public void setVendas(Set<VendaAberta> vendas) {
		this.vendas = vendas;
	}
	
	public Usuario getUsuarioCadastro() {
		return usuarioCadastro;
	}

	public void setUsuarioCadastro(Usuario usuarioCadastro) {
		this.usuarioCadastro = usuarioCadastro;
	}

	

	public void setFechado(boolean fechado) {
		this.fechado = fechado;
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
		Caixa other = (Caixa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
