package com.ebm.comercial.domain;

import com.ebm.exceptions.IlegalStateTransitionException;

public class OSCancelada implements OSEstadoOperations {
	
	
	@Override
	public OSEstado aprovar(OrdemServico os) {
		throw new IlegalStateTransitionException("Uma ordem cancelada não pode ter seu estado alterado, inicie uma nova ordem");	
	}

	@Override
	public OSEstado cancelar(OrdemServico os) {
		throw new IlegalStateTransitionException("Uma ordem cancelada não pode ter seu estado alterado, inicie uma nova ordem");	
	}

	@Override
	public OSEstado finalizar(OrdemServico os) {
		throw new IlegalStateTransitionException("Uma ordem cancelada não pode ter seu estado alterado, inicie uma nova ordem");	
	}

	@Override
	public OSEstado produzir(OrdemServico os) {
		throw new IlegalStateTransitionException("Uma ordem cancelada não pode ter seu estado alterado, inicie uma nova ordem");	
	}

	@Override
	public void emAtraso(OrdemServico os) {
		throw new IlegalStateTransitionException("Ordem ja foi cancelada");	
	}

	@Override
	public String getEstadoName() {
		return "Cancelada";
	}


}
