package com.onlineBanking.account.service;

import com.onlineBanking.account.entity.Account;
import com.onlineBanking.account.exception.AccountApplicationException;
import com.onlineBanking.account.request.BalanceDto;
import com.onlineBanking.account.request.CreateAccountRequestDto;

public interface AccountService {
    void createAccountWithCard(CreateAccountRequestDto createAccountRequestDto) throws AccountApplicationException;

	String updateAccountBalance(BalanceDto balanceDto) throws AccountApplicationException;
	Account findAccountByUserId(long userId) throws AccountApplicationException;
}
