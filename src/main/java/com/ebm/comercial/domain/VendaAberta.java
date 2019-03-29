package com.ebm.comercial.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.ebm.auth.Usuario;
import com.ebm.estoque.domain.OrigemMovimentacao;
import com.ebm.estoque.domain.ProdutoMovimentacao;
import com.ebm.pessoal.domain.Cliente;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class VendaAberta implements OrigemMovimentacao{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private BigDecimal descontoGeral;
	@Column(length = 240)
	private String obs;
	private LocalDateTime  abertura = LocalDateTime.now();
	@ManyToOne
	private Usuario usuarioCadastro;
	private LocalDateTime dataUltimaModificacao;
	@ManyToOne
	private Usuario ultimaModificacao;
	@OneToMany(mappedBy = "id.vendaAberta")
	private List<ProdutoVendaAberta> produtos = new ArrayList<ProdutoVendaAberta>();
	@JsonIgnore
	@ManyToOne
	private Caixa caixa;
	@ManyToOne
	private Cliente cliente;
	
	
	public VendaAberta() {}

	public VendaAberta(Integer id, BigDecimal descontoGeral, String obs, LocalDateTime abertura,
			Usuario usuarioCadastro) {
		super();
		this.id = id;
		this.descontoGeral = descontoGeral;
		this.obs = obs;
		this.abertura = abertura;
		this.usuarioCadastro = usuarioCadastro;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getDescontoGeral() {
		return descontoGeral;
	}

	public void setDescontoGeral(BigDecimal descontoGeral) {
		this.descontoGeral = descontoGeral;
	}

	public String getDescricao() {
		return obs;
	}
	public void setObs(String obs) {
		this.obs = obs;
	}

	public LocalDateTime getAbertura() {
		return abertura;
	}

	public void setAbertura(LocalDateTime abertura) {
		this.abertura = abertura;
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

	public List<ProdutoVendaAberta> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<ProdutoVendaAberta> produtos) {
		this.produtos = produtos;
	}
	public double getValorTotal() {
		return produtos.stream().mapToDouble(ProdutoVendaAberta::getSubTotal).sum();
	}
	public double getLucroTotal() {
		return produtos.stream().mapToDouble(ProdutoVendaAberta::getLucroUnitario).sum();
	}
	
	@Override
	public Cliente getCliente() {
		return this.cliente;
	}

	@Override
	public Usuario getUsuario() {
		// TODO Auto-generated method stub
		return this.usuarioCadastro;
	}

	@Override
	public List<FuncionarioFuncao> getFuncionariosComFuncao() {
		// TODO Auto-generated method stub
		return Arrays.asList(new FuncionarioFuncao(this.getUsuario().getFuncionario(), "Vendedor"));
	}

	@Override
	public String getDocumento() {
		// TODO Auto-generated method stub
		return "Venda Aberta, caixa código: " + caixa.getCodigoCaixa() + ", venda id: " + this.id;
	}

	@Override
	public LocalDate getDataMovimentacao() {
		// TODO Auto-generated method stub
		return this.abertura.toLocalDate();
	}

	@Override
	public List<ProdutoMovimentacao> getProdutosMovimentacao() {

		return produtos.stream().map( x-> new ProdutoMovimentacao(x.getProduto(), x.getQuantidade(), x.getValorUnitarioComDesconto())).collect(Collectors.toList());
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
		VendaAberta other = (VendaAberta) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
}
