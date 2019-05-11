package com.ebm.comercial.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.ebm.auth.Usuario;
import com.ebm.estoque.domain.Produto;
import com.ebm.estoque.domain.ProdutoMovimentacao;
import com.ebm.estoque.domain.interfaces.OrigemMovimentacao;
import com.ebm.pessoal.domain.Cliente;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Venda implements Serializable, OrigemMovimentacao{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	private LocalDate dataAbertura;
	private BigDecimal descontoGeral;
	@ManyToOne(fetch = FetchType.LAZY)
	private Cliente cliente;
	@OneToOne
	private Usuario autorizouDesconto;
	private String observacao;
	@OneToMany(mappedBy="id.venda")
	private Set<ProdutoVenda> produtosVenda  = new HashSet<ProdutoVenda>();
	private LocalDateTime  dataCadastro = LocalDateTime.now();
	@ManyToOne
	private Usuario usuarioCadastro;
	private LocalDateTime dataUltimaModificacao;
	@ManyToOne
	private Usuario ultimaModificacao;
	private boolean jaDeuBaixa;
	private boolean jaDevolveu;
	private boolean aprovada;
	
	public Venda() {}

	public Venda(Integer id, LocalDate dataAbertura, BigDecimal descontoGeral, Cliente cliente,
			Usuario autorizouDesconto, String observacao) {
		this.id = id;
		this.dataAbertura = dataAbertura;
		this.descontoGeral = descontoGeral;
		this.cliente = cliente;
		this.autorizouDesconto = autorizouDesconto;
		this.observacao = observacao;
	}
	
	public double getValorTotal() {
		return produtosVenda.stream().mapToDouble(x-> x.getSubTotal()).sum(); 
	}

	public Integer getId() {
		return id;
	}
	
	public boolean isAprovada() {
		return aprovada;
	}

	public void setAprovada(boolean aprovada) {
		
		this.aprovada = aprovada;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public boolean isJaDeuBaixa() {
		return jaDeuBaixa;
	}
	
	public void setJaDeuBaixa(boolean jaDeuBaixa) {
		this.jaDeuBaixa = jaDeuBaixa;
	}

	public boolean isJaDevolveu() {
		return jaDevolveu;
	}

	public void setJaDevolveu(boolean jaDevolveu) {
		this.jaDevolveu = jaDevolveu;
	}

	public Usuario getUsuarioCadastro() {
		return usuarioCadastro;
	}

	public LocalDate getDataAbertura() {
		return dataAbertura;
	}

	public void setDataAbertura(LocalDate dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	public BigDecimal getDescontoGeral() {
		return descontoGeral;
	}

	public void setDescontoGeral(BigDecimal descontoGeral) {
		this.descontoGeral = descontoGeral;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Set<ProdutoVenda> getProdutosVenda() {
		return produtosVenda;
	}

	public void setProdutosVenda(Set<ProdutoVenda> produtosVenda) {
		this.produtosVenda = produtosVenda;
	}

	public Usuario getAutorizouDesconto() {
		return autorizouDesconto;
	}

	public void setAutorizouDesconto(Usuario autorizouDesconto) {
		this.autorizouDesconto = autorizouDesconto;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	public List<Produto> getProdutos() {
		return produtosVenda.stream().map(x -> x.getProduto()).collect(Collectors.toList());
	}

	public LocalDateTime getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(LocalDateTime dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Usuario getUsuario() {
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

	public List <FuncionarioFuncao> getFuncionariosComFuncao() {
		 Optional.of(usuarioCadastro).orElseThrow( () -> new RuntimeException("Nao possui usuarioCadastro"));
		
		return  Arrays.asList(new FuncionarioFuncao(usuarioCadastro.getFuncionario(), "Vendedor"));
	}
	public double getLucroProdutos() {
		return this.produtosVenda.stream().mapToDouble(x -> x.getSubLucroTotal()).sum();
	}
	public String getDescricao() {
		return this.observacao;
	}
	@Override
	public String getDocumento() {
		return "Venda fechada, id = " + this.getId();
	}

	@Override
	public LocalDate getDataMovimentacao() {
	
		return this.getDataAbertura();
	}

	@Override
	public List<ProdutoMovimentacao> getProdutosMovimentacao() {
		return produtosVenda.stream().map(x -> new ProdutoMovimentacao(x.getProduto(), x.getQuantidade(), 
				BigDecimal.valueOf(x.getValorVendaSemDesconto()).subtract(BigDecimal.valueOf(x.getDescontoUnitario()))))
				  .collect(Collectors.toList());
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
		Venda other = (Venda) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


}
