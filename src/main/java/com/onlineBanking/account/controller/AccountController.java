package com.onlineBanking.account.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlineBanking.account.entity.Account;
import com.onlineBanking.account.exception.AccountApplicationException;
import com.onlineBanking.account.request.CreateAccountRequestDto;
import com.onlineBanking.account.service.AccountService;


@RestController
@RequestMapping("api/v1")
public class AccountController {
	
	private final AccountService accountService;
	
	@Autowired	
	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}


	@PostMapping("/create-account")
	ResponseEntity<String> createAccount(@RequestBody CreateAccountRequestDto request) throws AccountApplicationException{
	    String response = accountService.createAccount(request.getUserId(), request.getAccountType());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
    @GetMapping("get-accounts")
    public ResponseEntity<List<Account>> getAllStudentMarks() {
        List<Account> studentMarksList = accountService.getAllAccounts();
        if (studentMarksList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(studentMarksList);
        }
        return ResponseEntity.ok(studentMarksList);
    }
	

	@PatchMapping("/update-balance")
	ResponseEntity<String> updateBalance(){
		String response = accountService.updateBalance();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
