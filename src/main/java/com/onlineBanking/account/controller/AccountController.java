package com.onlineBanking.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlineBanking.account.service.AccountService;

@RestController
@RequestMapping("api/v1")
public class AccountController {
	
	private final AccountService accountService;
	
	@Autowired	
	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}



	@PatchMapping("/update-balance")
	ResponseEntity<String> updateBalance(){
		String response = accountService.updateBalance();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
