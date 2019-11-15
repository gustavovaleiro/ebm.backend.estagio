package com.ebm.geral.resource.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ebm.geral.exceptions.AuthorizationException;
import com.ebm.geral.exceptions.DataIntegrityException;
import com.ebm.geral.exceptions.ObjectNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request){
	  StandardError standardError = new StandardError( System.currentTimeMillis(), HttpStatus.NOT_FOUND.value(), "Não encontrado", e.getMessage(), request.getRequestURI());
	  return ResponseEntity.status(HttpStatus.NOT_FOUND).body(standardError);
	}
	@ExceptionHandler(DataIntegrityException.class)
	public ResponseEntity<StandardError> dataIntegrity(DataIntegrityException e, HttpServletRequest request){
	  StandardError standardError = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(),"Integridade de dados", e.getMessage(), request.getRequestURI());	  return ResponseEntity.status( HttpStatus.BAD_REQUEST).body(standardError);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e, HttpServletRequest request){
	  ValidationError err = new ValidationError(System.currentTimeMillis(), HttpStatus.UNPROCESSABLE_ENTITY.value(),"Dados Invalidos", e.getMessage(), request.getRequestURI());;
	 for(FieldError error : e.getBindingResult().getFieldErrors()) {
		 err.addError(error.getField(), error.getDefaultMessage());
	 }
	  
	  return ResponseEntity.status( HttpStatus.UNPROCESSABLE_ENTITY).body(err);
	}
	@ExceptionHandler(AuthorizationException.class)
	public ResponseEntity<StandardError> auhtorizathion(AuthorizationException e, HttpServletRequest request){
		 StandardError standardError = new StandardError(System.currentTimeMillis(), HttpStatus.FORBIDDEN.value(),"Acesso Negado", e.getMessage(), request.getRequestURI());
		  return ResponseEntity.status(HttpStatus.FORBIDDEN).body(standardError);
	}
}
