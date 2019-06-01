package com.ebm.estoque.dtos;

import java.io.Serializable;

import com.ebm.estoque.domain.Movimentacao;
import com.ebm.geral.utils.Utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovimentacaoListDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String tipo;
	private String data;
	private String documento;

	public MovimentacaoListDTO(Movimentacao movimentacao) {
		this(movimentacao.getId(), movimentacao.getTipoMovimentacao().getDesc(),
				movimentacao.getDataMovimentacao().format(Utils.getBrDateTimeFormatter()), movimentacao.getDocumento());
	}

}
