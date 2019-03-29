package com.ebm.comercial.domain;

public enum OSEstado implements OSEstadoOperations {

	ABERTA(1,new OSAberta()),
	APROVADA(2,new OSAprovada()),
	CANCELADA(3,new OSCancelada()),
	FINALIZADA(4,new OSCancelada()),
	PRODUCAO(5,new OSProducao());
	
	
	private final OSEstadoOperations operations;
	private int  cod;
	OSEstado(int cod,OSEstadoOperations operations) {
		this.operations = operations;
		this.cod = cod;
	}

	@Override
	public OSEstado aprovar(OrdemServico os) {
		return this.operations.aprovar(os);
		
	}

	@Override
	public OSEstado cancelar(OrdemServico os) {
		return this.operations.cancelar(os);
		
	}

	@Override
	public OSEstado finalizar(OrdemServico os) {
		return this.operations.finalizar( os);
		
	}

	@Override
	public OSEstado produzir(OrdemServico os) {
		return this.operations.produzir( os);
		
	}


	public void emAtraso(OrdemServico os) {
		 this.operations.emAtraso(os);
		
	}

	@Override
	public String getEstadoName() {
		return this.operations.toString();
	}

	public Integer getCod() {
		return cod;
	}
	
	public static OSEstado toEnum(Integer cod) {
		if(cod == null) {
			return null;
		}
		for(OSEstado x: OSEstado.values()) {
			if(cod.equals(x.getCod()))
				return x;
		}
		
		throw new IllegalArgumentException("id invalido: " + cod);
	}

}
