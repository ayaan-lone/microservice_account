package com.onlineBanking.account.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.onlineBanking.account.dao.AccountRepository;
import com.onlineBanking.account.entity.Account;
import com.onlineBanking.account.request.CreateCardDto;
import com.onlineBanking.account.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private AccountRepository accountRepository;

	@Override
	public void createAccountWithCard(long userId, String accountType) {
		// Create account logic
		Account account = new Account();
		account.setUserId(userId);
		account.setAccountType(accountType);
		account.setBalance(0);

		// Save account
		accountRepository.save(account);

		CreateCardDto request = new CreateCardDto(userId, accountType);
		HttpEntity<CreateCardDto> httpEntity = new HttpEntity<CreateCardDto>(request);
		String cardServiceUrl = "http://localhost:8082/api/v1/create";
		restTemplate.exchange(cardServiceUrl, HttpMethod.POST, httpEntity, Object.class);
	}
}
