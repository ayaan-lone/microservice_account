package com.onlineBanking.account.service;

import com.onlineBanking.account.entity.Account;
import com.onlineBanking.account.exception.AccountApplicationException;
import com.onlineBanking.account.request.CreateAccountRequestDto;
import com.onlineBanking.account.request.UpdateBalanceRequestDto;

public interface AccountService {
    String createAccountWithCard(CreateAccountRequestDto createAccountRequestDto) throws AccountApplicationException;

	Account findAccountByUserId(long userId) throws AccountApplicationException;

	Double getAccountBalance(long userId) throws AccountApplicationException;

	String updateBalance(UpdateBalanceRequestDto updateBalanceRequestDto) throws AccountApplicationException;
}
