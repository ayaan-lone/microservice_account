package com.onlineBanking.account.service;

import com.onlineBanking.account.exception.AccountApplicationException;

public interface AccountService {
    void createAccountWithCard(long userId, long accountId) throws AccountApplicationException;
}
