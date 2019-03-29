package com.ebm.exceptions;

public class IlegalStateTransitionException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public IlegalStateTransitionException(String msg) {
		super(msg);
	}
	
	public IlegalStateTransitionException(String msg, Throwable cause) {
		super(msg,cause);
	}

}
