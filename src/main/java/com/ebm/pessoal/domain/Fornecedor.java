package com.ebm.pessoal.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import com.ebm.estoque.domain.CategoriaItem;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Fornecedor implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;

	@MapsId
	@OneToOne
	private Pessoa pessoa;

	@ManyToMany
	private Set<CategoriaItem> categorias = new HashSet<CategoriaItem>();

	public Fornecedor(Integer id, Pessoa pessoa) {
		this.id = id;
		this.pessoa = pessoa;
	}

	public static Fornecedor ofId(Integer id) {
		Fornecedor fornecedor = new Fornecedor();
		fornecedor.setId(id);
		return fornecedor;
	}
}
