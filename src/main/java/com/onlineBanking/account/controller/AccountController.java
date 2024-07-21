package com.onlineBanking.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.onlineBanking.account.entity.Account;
import com.onlineBanking.account.exception.AccountApplicationException;
import com.onlineBanking.account.request.CreateAccountRequestDto;
import com.onlineBanking.account.request.UpdateBalanceRequestDto;
import com.onlineBanking.account.service.AccountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class AccountController {

	@Autowired
	private AccountService accountService;

	// Create a new account and generate a Card
	@PostMapping("/create")
	public void createAccount(@Valid @RequestBody CreateAccountRequestDto createAccountRequestDto)
			throws AccountApplicationException {
		accountService.createAccountWithCard(createAccountRequestDto);
	}

	// To fetch all the accounts associated with a user
	@GetMapping("/account-detail")
	public ResponseEntity<Account> getAccountByUserId(@RequestParam(required = true) long userId) throws AccountApplicationException {
		Account response = accountService.findAccountByUserId(userId);
		return ResponseEntity.status(HttpStatus.OK).body(response);

	}

	@GetMapping("/balance")
	public ResponseEntity<Double> getAccountBalance(@RequestParam(required = true) long userId)
			throws AccountApplicationException {
		Double response = accountService.getAccountBalance(userId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping("/update-balance")
	public ResponseEntity<String> updateAccountBalance(@RequestBody UpdateBalanceRequestDto updateBalanceRequestDto)
			throws AccountApplicationException {

		String response = accountService.updateBalance(updateBalanceRequestDto);
		return ResponseEntity.status(HttpStatus.OK).body(response);

	}
}
