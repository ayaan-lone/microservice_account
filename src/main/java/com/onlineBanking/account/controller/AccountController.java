package com.onlineBanking.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.onlineBanking.account.exception.AccountApplicationException;
import com.onlineBanking.account.service.AccountService;

@RestController
@RequestMapping("/api/v1")
public class AccountController {

	@Autowired
	private AccountService accountService;

	// Create a new account and generate a Card
	@PostMapping("/create")
	public void createAccount(@RequestParam long userId, @RequestParam long accountId) throws AccountApplicationException {
		accountService.createAccountWithCard(userId, accountId);
	}
}
