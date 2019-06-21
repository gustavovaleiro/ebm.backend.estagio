package com.ebm.estoque.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.ebm.estoque.domain.enums.TipoMovimentacao;
import com.ebm.pessoal.domain.Fornecedor;
import com.ebm.pessoal.domain.HistoricoCadastral;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
	@Length(min = 2, max = 40, message = "O campo documento tem que ter um tamanho de 2 a 40 caracteres")
	private String documento;
	@Column(length = 400)
	@Length(max = 600, message = "O campo descricao tem que ter no maximo 600 caracteres")
	private String descricao;
	 @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dataMovimentacao;
	
	@Enumerated(EnumType.STRING)
	@NotNull(message = "O campo tipo de movimentação não pode ficar vazio, escolha entre Entrada e Saida")
	private TipoMovimentacao tipoMovimentacao;
	
	@ManyToMany
	private Set<Fornecedor> fornecedores = new HashSet<>();
	
	@OneToMany(mappedBy="id.movimentacao")
	@NotNull(message = "O campo de produtos associados a movimentação não pode ser nulo ou vazio")
	@NotEmpty(message = "O campo de produtos associados a movimentação não pode ser nulo ou vazio")
	private Set<ProdutoMovimentacao> produtoMovimentacao = new HashSet<>();
	@Embedded
	private HistoricoCadastral historico= new HistoricoCadastral();

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
	
	@JsonIgnore
	@Transient
	public Set<Produto> getProdutos() {
		return produtoMovimentacao.stream().map(p -> p.getProduto()).collect(Collectors.toSet());
	}
	
	public static Movimentacao novaEntrada() {
		return new Movimentacao(TipoMovimentacao.ENTRADA);
	}
	public static Movimentacao novaSaida() {
		return new Movimentacao(TipoMovimentacao.SAIDA);
	}

}
