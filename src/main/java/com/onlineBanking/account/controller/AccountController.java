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
import com.onlineBanking.account.request.BalanceDto;
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
	
	@PostMapping("/update")
    ResponseEntity<String> updateBalance(@RequestBody BalanceDto balanceDto) throws AccountApplicationException{
    	String response = accountService.updateAccountBalance(balanceDto);
    	return ResponseEntity.status(HttpStatus.OK).body(response);
    }
	//To fetch all the accounts associated with a user
    @GetMapping("/account-detail")
    public Account getAccountByUserId(@RequestParam long userId) throws AccountApplicationException {
        
    	return accountService.findAccountByUserId(userId);
    }
}
