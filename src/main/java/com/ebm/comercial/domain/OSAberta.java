package com.ebm.comercial.domain;

import java.time.LocalDate;

import com.ebm.exceptions.IlegalStateTransitionException;

public class OSAberta implements OSEstadoOperations{
	
	public OSAberta() {}

	@Override
	public OSEstado aprovar(OrdemServico os) {
		os.setAprovada(true);
		return OSEstado.APROVADA;
		
	}

	@Override
	public OSEstado cancelar(OrdemServico os) {
		return OSEstado.CANCELADA;
	}

	@Override
	public OSEstado finalizar(OrdemServico os) {
		throw new IlegalStateTransitionException("Você não pode alterar uma Ordem do estado Aberto para finalizado, únicas transições permitidas são: aprovar, cancelar.");		
	}

	@Override
	public OSEstado produzir(OrdemServico os) {
		throw new IlegalStateTransitionException("Você não pode alterar uma Ordem do estado Aberto para produção, únicas transições permitidas são: aprovar, cancelar.");	
		
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
		return "Aberta";
	}

}
