package com.onlineBanking.account.service;

import java.util.List;

import com.onlineBanking.account.entity.Account;
import com.onlineBanking.account.exception.AccountApplicationException;

public interface AccountService {
	String updateBalance();

	String createAccount(String userId, String accountType) throws AccountApplicationException;

	List<Account> getAllAccounts();
}
