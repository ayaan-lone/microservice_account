package com.onlineBanking.account.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlineBanking.account.exception.AccountApplicationException;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
public class HealthCheckController {
	
	@GetMapping("health-check")
	ResponseEntity<String> healthCheck(HttpServletRequest request) throws AccountApplicationException {
		Long userId = (Long) request.getAttribute("userId");
		System.out.println(userId);
		return ResponseEntity.status(HttpStatus.OK).body("Account Microservice is working");
	}

}
