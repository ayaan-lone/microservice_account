package com.onlineBanking.account.exception;

import org.springframework.http.HttpStatus;

public class AccountApplicationException extends Exception {

	private static final long serialVersionUID = -5421249725272116443L;
	
	private HttpStatus httpStatus;
	private String message;
	
	public AccountApplicationException(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}	

}
