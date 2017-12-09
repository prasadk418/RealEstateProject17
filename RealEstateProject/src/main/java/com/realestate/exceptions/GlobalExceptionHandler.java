package com.realestate.exceptions;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity customExceptionHandler(HttpServletRequest req, Exception e)
	{
		return new ResponseEntity("{ \"message\" : \" "+e.getMessage() + " \" }", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity globalExceptionHandler(HttpServletRequest req, Exception e)
	{
		return new ResponseEntity("{ \"message\" : \" "+e.getMessage() + " \" }", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
