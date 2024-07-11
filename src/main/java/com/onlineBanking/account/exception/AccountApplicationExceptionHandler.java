package com.onlineBanking.account.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AccountApplicationExceptionHandler {

	@ExceptionHandler(value = { AccountApplicationException.class })
	ResponseEntity<Object> handleAccountApplicationException(AccountApplicationException accountApplicationException) {
		return ResponseEntity.status(accountApplicationException.getHttpStatus())
				.body(accountApplicationException.getMessage());
	}

}
