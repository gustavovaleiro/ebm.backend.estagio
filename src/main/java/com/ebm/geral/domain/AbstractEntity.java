package com.ebm.geral.domain;

public abstract class AbstractEntity<ID> {
	public abstract ID getId();
	public abstract void setId(ID id);
}
