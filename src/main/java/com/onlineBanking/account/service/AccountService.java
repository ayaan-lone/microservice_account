package com.onlineBanking.account.service;

import com.onlineBanking.account.exception.AccountApplicationException;
import com.onlineBanking.account.request.BalanceDto;

public interface AccountService {
    void createAccountWithCard(long userId, long accountId) throws AccountApplicationException;

	String updateAccountBalance(BalanceDto balanceDto) throws AccountApplicationException;
}
