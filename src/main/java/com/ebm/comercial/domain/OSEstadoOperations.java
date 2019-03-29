package com.ebm.comercial.domain;

public interface OSEstadoOperations {
	public OSEstado aprovar(OrdemServico os);
	public OSEstado cancelar(OrdemServico os);
	public OSEstado finalizar(OrdemServico os);
	public OSEstado produzir(OrdemServico os);
	public void emAtraso(OrdemServico os);
	String getEstadoName();
}
