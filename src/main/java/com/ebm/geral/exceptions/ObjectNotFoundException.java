package com.ebm.geral.exceptions;
public class ObjectNotFoundException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT = "Não foi possivel encontrar";
	public ObjectNotFoundException(String msg) {
		super(msg);
	}
	
	public ObjectNotFoundException(String msg, Throwable cause) {
		super(msg,cause);
	}

	public ObjectNotFoundException() {
		super();
	}
}
