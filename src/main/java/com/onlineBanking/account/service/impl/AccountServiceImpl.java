package com.onlineBanking.account.service.impl;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.onlineBanking.account.dao.AccountRepository;
import com.onlineBanking.account.entity.Account;
import com.onlineBanking.account.exception.AccountApplicationException;
import com.onlineBanking.account.request.BalanceDto;
import com.onlineBanking.account.request.CreateCardDto;
import com.onlineBanking.account.service.AccountService;
import com.onlineBanking.account.util.ConstantUtils;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private AccountRepository accountRepository;

	@Override
	public void createAccountWithCard(long userId, long accountId) throws AccountApplicationException {
		// Create account logic
		Account account = new Account();
		account.setUserId(userId);
		// Retrieve account type from metadata microservice
		String accountType = fetchAccountTypeFromMetadata(accountId);
		account.setAccountType(accountType);
		account.setBalance(0);
		account.setAccountNo(Math.abs(new Random().nextLong() % 10000000000000000L));

		// Save account
		accountRepository.save(account);
		// Create a cardDto
		CreateCardDto request = new CreateCardDto(userId, accountId);
		HttpEntity<CreateCardDto> httpEntity = new HttpEntity<CreateCardDto>(request);
		// Send the DTO to our restTemplate to create a card
		restTemplate.exchange(ConstantUtils.CARD_SERVICE_URL, HttpMethod.POST, httpEntity, Object.class);
	}

	private String fetchAccountTypeFromMetadata(long accountId) throws AccountApplicationException {
		String metadataUrl = ConstantUtils.METADATA_SERVICE_URL + accountId;
		ResponseEntity<String> response = restTemplate.getForEntity(metadataUrl, String.class);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new AccountApplicationException(HttpStatus.NOT_FOUND, ConstantUtils.ACCOUNT_NOT_FOUND);
		}
		return response.getBody();
	}
	
	
	@Override
	public String updateAccountBalance(BalanceDto balanceDto) {
		Account account = accountRepository.findByUserId(balanceDto.getUserId());
		System.out.println("Balance dto is: " + balanceDto.getUserId());
		if(balanceDto.getTransactionType().equals("credit")) {
			account.setBalance(account.getBalance() + balanceDto.getAmount());
		}else {
			account.setBalance(account.getBalance() - balanceDto.getAmount());
		}
		accountRepository.save(account);
		return "Balance have been updated";
	}
}
