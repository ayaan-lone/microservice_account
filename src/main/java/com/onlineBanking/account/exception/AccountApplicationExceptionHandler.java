package com.onlineBanking.account.exception;

import org.springframework.dao.DataIntegrityViolationException;
import io.jsonwebtoken.JwtException; 
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class AccountApplicationExceptionHandler {

	@ExceptionHandler(value = { AccountApplicationException.class })
	ResponseEntity<Object> handleAccountApplicationException(AccountApplicationException accountApplicationException) {
		return ResponseEntity.status(accountApplicationException.getHttpStatus())
				.body(accountApplicationException.getMessage());
	}
	
	@ExceptionHandler(value = {DataIntegrityViolationException.class})
	ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException  dataIntegrityViolationException){
		return ResponseEntity.status(HttpStatus.CONFLICT).body("This user Already has an account");
	}

	@ExceptionHandler(value = { HttpClientErrorException.class })
	ResponseEntity<Object> handleHttpClientErrorException(HttpClientErrorException httpClientErrorException) {
		return ResponseEntity.status(httpClientErrorException.getStatusCode())
				.body(httpClientErrorException.getMessage());
	}
	
	@ExceptionHandler(JwtException.class)
    public ResponseEntity<String> handleJwtException(JwtException ex) {
        // Return a custom error message and a status code of your choice
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT Error: " + ex.getMessage());
    }

}
