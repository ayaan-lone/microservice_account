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
import com.onlineBanking.account.entity.TransactionType;
import com.onlineBanking.account.exception.AccountApplicationException;
import com.onlineBanking.account.request.CreateAccountRequestDto;
import com.onlineBanking.account.request.CreateCardRequestDto;
import com.onlineBanking.account.request.UpdateBalanceRequestDto;
import com.onlineBanking.account.service.AccountService;
import com.onlineBanking.account.util.ConstantUtils;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	public RestTemplate restTemplate;

	@Autowired
	public AccountRepository accountRepository;

	@Override
	public void createAccountWithCard(CreateAccountRequestDto createAccountRequestDto) throws AccountApplicationException {
		// Create account logic
		Account account = new Account();
		account.setUserId(createAccountRequestDto.getUserId());
		// Retrieve account type from metadata microservice
		String accountType = fetchAccountTypeFromMetadata(createAccountRequestDto.getAccountId());
		account.setAccountType(accountType);
		account.setBalance(0.0);
		account.setAccountNo(Math.abs(new Random().nextLong() % 10000000000000000L));

		// Save account
		accountRepository.save(account);
		// Create a cardDto
		CreateCardRequestDto request = new CreateCardRequestDto(createAccountRequestDto.getUserId(), createAccountRequestDto.getAccountId(), createAccountRequestDto.getCardId());
		HttpEntity<CreateCardRequestDto> httpEntity = new HttpEntity<CreateCardRequestDto>(request);
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

	
// Fetch Account Detail by  userId
	@Override
	public Account findAccountByUserId(long userId) throws AccountApplicationException {
		// TODO Auto-generated method stub
		
		return accountRepository.findByUserId(userId);
	}

	@Override
	public Double getAccountBalance(long userId) {
		Account account = accountRepository.findByUserId(userId);
		return account.getBalance();
	}

	@Override
	public String updateBalance(UpdateBalanceRequestDto updateBalanceRequestDto)  {
		
		Account account = accountRepository.findByUserId(updateBalanceRequestDto.getUserId());
		
		if(updateBalanceRequestDto.getTransactionType().equals(TransactionType.CREDIT)) {
			account.setBalance(account.getBalance()+updateBalanceRequestDto.getAmount());
		}else {
			account.setBalance(account.getBalance()-updateBalanceRequestDto.getAmount());
		}
		accountRepository.save(account);
		return "Balance have been updated";
	}
	
	
	
}
