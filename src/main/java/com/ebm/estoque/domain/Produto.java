package com.ebm.estoque.domain;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.ebm.estoque.domain.enums.TipoItem;
import com.ebm.geral.utils.Utils;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("P")
public class Produto extends Item {
	private static final long serialVersionUID = 1L;

	@NotNull(message = "O estoque minimo é um campo obrigatório")
	private Integer estoqueMinimo;
	private Integer estoqueMax;
	private Integer estoqueAtual;
	private Double peso;
	private Double altura;
	private Double largura;
	private Double comprimento;

	@JsonIgnore
	@OneToMany(mappedBy = "id.produto")
	private Set<ProdutoMovimentacao> entradas = new HashSet<>();
	@JsonIgnore
	@OneToMany(mappedBy = "id.produto")
	private Set<ProdutoMovimentacao> saidas = new HashSet<>();

	public static Produto of(String nome, Unidade un, CategoriaItem categoria) {
		return new Produto(null, nome, nome, un, categoria, Utils.getRandomCodInterno(TipoItem.PRODUTO, nome), null,
				null, null, null, null, null, null);
	}

	public Produto() {
		super.tipo = TipoItem.PRODUTO.getDescricao();
	}

	public Produto(Integer id, String nome, String descricao, Unidade unidade, CategoriaItem categoria,
			String codInterno, BigDecimal valorCompraMedio, BigDecimal outrasDespesa, Double margemLucro,
			Double comissaoVenda, Integer estoqueMinimo, Integer estoqueAtual, Integer estoqueMaximo) {

		super(id, nome, descricao, unidade, categoria, codInterno, valorCompraMedio, outrasDespesa, margemLucro,
				comissaoVenda);
		super.tipo = TipoItem.PRODUTO.getDescricao();
		this.estoqueMinimo = estoqueMinimo;
		this.estoqueAtual = estoqueAtual;
		this.estoqueMax = estoqueMaximo;
	}

	public void setEstoque(int min, int atual, int max) {
		this.estoqueMinimo = min;
		this.estoqueAtual = atual;
		this.estoqueMax = max;
	}

	public static Produto ofId(Integer id) {
		Produto produto = new Produto();
		produto.setId(id);
		return produto;
	}

}
