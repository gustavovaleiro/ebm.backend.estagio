package com.ebm.estoque.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.ebm.estoque.domain.enums.TipoMovimentacao;
import com.ebm.pessoal.domain.Fornecedor;
import com.ebm.security.Usuario;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public  class Movimentacao implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;
	@Column(length = 20)
	private String documento;
	@Column(length = 400)
	private String descricao;
	
	private LocalDateTime dataMovimentacao;
	
	private LocalDateTime  dataCadastro;
	@ManyToOne
	private Usuario usuarioCadastro;
	private LocalDateTime dataUltimaModificacao;
	@ManyToOne
	private Usuario ultimaModificacao;
	
	@Enumerated(EnumType.STRING)
	private TipoMovimentacao tipoMovimentacao;
	
	@ManyToMany
	private Set<Fornecedor> fornecedores = new HashSet<>();
	
	@OneToMany(mappedBy="id.movimentacao")
	private Set<ProdutoMovimentacao> produtoMovimentacao = new HashSet<>();
	

	public Movimentacao(TipoMovimentacao tipoMovimentacao) {
		this.tipoMovimentacao = tipoMovimentacao;
	}
	public Movimentacao(Integer id, String documento, String descricao, LocalDateTime dataMovimentacao) {
		super();
		this.id = id;
		this.documento = documento;
		this.descricao = descricao;
		this.dataMovimentacao = dataMovimentacao;
	}
	
	
	public static Movimentacao novaEntrada() {
		return new Movimentacao(TipoMovimentacao.ENTRADA);
	}
	public static Movimentacao novaSaida() {
		return new Movimentacao(TipoMovimentacao.SAIDA);
	}

}
