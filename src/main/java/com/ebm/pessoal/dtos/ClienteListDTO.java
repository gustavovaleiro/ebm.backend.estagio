package com.ebm.pessoal.dtos;

import java.io.Serializable;

import com.ebm.pessoal.domain.Cliente;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteListDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String nome;
	private String tipo;
	private Double limteCompra;
	private String telefone;
	private String email;
	private String cidade;

	public ClienteListDTO(Cliente c) {
		this(c.getId(), c.getPessoa().getNome(), c.getPessoa().getTipo().getDescricao(),
				c.getLimiteCompra().doubleValue(), c.getPessoa().getTelefonePrincipal().toString(),
				c.getPessoa().getEmailPrincipal().getEmail(),
				c.getPessoa().getEnderecoPrincipal().getCidade().getEstado().getUF() + " - " + c.getPessoa().getEnderecoPrincipal().getCidade().getNome());
	}

}
