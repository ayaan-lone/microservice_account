package com.onlineBanking.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.onlineBanking.account.service.AccountService;

@RestController
@RequestMapping("/api/v1")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/create")
    public void createAccount(@RequestParam long userId, @RequestParam String accountType) {
        accountService.createAccountWithCard(userId, accountType);
    }
}
