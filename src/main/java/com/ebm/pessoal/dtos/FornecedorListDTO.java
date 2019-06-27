package com.ebm.pessoal.dtos;

import java.io.Serializable;

import com.ebm.pessoal.domain.Fornecedor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FornecedorListDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String tipo;
	private String cpfCnpj;
	private String nome;
	private String telefone;
	private String email;
	private String cidade;

	

	public FornecedorListDTO(Fornecedor fornecedor) {
		this(fornecedor.getId(),
				fornecedor.getPessoa().getTipo().getDescricao(),
				fornecedor.getPessoa().getDocument(),
				fornecedor.getPessoa().getNome(),
				fornecedor.getPessoa().getTelefonePrincipal().toString(),
				fornecedor.getPessoa().getEmailPrincipal().getEmail(),
				fornecedor.getPessoa().getEnderecoPrincipal().getCidade().getNome());
	}

}
