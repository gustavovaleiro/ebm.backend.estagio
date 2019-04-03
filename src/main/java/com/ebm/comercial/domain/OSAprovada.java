package com.ebm.comercial.domain;

import java.time.LocalDate;

import com.ebm.exceptions.IlegalStateTransitionException;

public class OSAprovada implements OSEstadoOperations {
	@Override
	public OSEstado aprovar(OrdemServico os) {
		throw new IlegalStateTransitionException("Você não pode aprovar uma ordem que ja está aprovada, únicas transiçoes permitidas são: produzir, cancelar.");		
	}


	@Override
	public OSEstado cancelar(OrdemServico os) {
		os.setAprovada(false);
		 return OSEstado.CANCELADA;
	}

	@Override
	public OSEstado finalizar(OrdemServico os) {
		throw new IlegalStateTransitionException("Você não pode finilizar uma ordem que ja está aprovada, únicas transiçoes permitidas são: produzir, cancelar.");		
	}

	@Override
	public OSEstado produzir(OrdemServico os) {
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
