package com.realestate.util;

import java.io.Serializable;

public class MyResponse implements Serializable{

	private static final long serialVersionUID = 123456L;
	
	private String message;
	private boolean status;
	

	public MyResponse(String message, boolean status) {
		this.message=message;
		this.status=status;
	}
	

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	

	
}
