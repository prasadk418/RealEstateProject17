package com.realestate.exceptions;

import java.io.Serializable;

public class CustomException extends Exception implements Serializable{

	private static final long serialVersionUID = 123456L;

	private String message;
	public CustomException()
	{		
	}

	public CustomException(String message)
	{
		this.message=message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
