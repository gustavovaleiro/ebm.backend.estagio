package com.ebm.geral.exceptions;


public class DataIntegrityException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT = "Erro de integridade dos dados";
	
	public DataIntegrityException(String msg) {
		super(msg);
	}
	
	public DataIntegrityException(String msg, Throwable cause) {
		super(msg,cause);
	}
}
