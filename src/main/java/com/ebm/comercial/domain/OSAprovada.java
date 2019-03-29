package com.ebm.comercial.domain;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;

import com.ebm.estoque.domain.service.MovimentacaoService;
import com.ebm.exceptions.IlegalStateTransitionException;

public class OSAprovada implements OSEstadoOperations {
	@Autowired
	private MovimentacaoService movimentacaoService;
	
	@Override
	public OSEstado aprovar(OrdemServico os) {
		throw new IlegalStateTransitionException("Você não pode aprovar uma ordem que ja está aprovada, únicas transiçoes permitidas são: produzir, cancelar.");		
	}


	@Override
	public OSEstado cancelar(OrdemServico os) {
		os.setAprovada(false);
		movimentacaoService.cancelarBaixaEstoque(os);
		 return OSEstado.CANCELADA;
	}

	@Override
	public OSEstado finalizar(OrdemServico os) {
		throw new IlegalStateTransitionException("Você não pode finilizar uma ordem que ja está aprovada, únicas transiçoes permitidas são: produzir, cancelar.");		
	}

	@Override
	public OSEstado produzir(OrdemServico os) {
		movimentacaoService.baixaEstoque(os);
		return OSEstado.PRODUCAO;
	}

	@Override
	public void emAtraso(OrdemServico os) {
		if(os.getDataTerminoPrevista().isBefore(LocalDate.now())) {
			os.setAtraso(true);
		}else
			throw new IlegalStateTransitionException("A OS ainda não está fora do prazo");	
	}

	@Override
	public String getEstadoName() {
		return "Aprovada";
	}



}
