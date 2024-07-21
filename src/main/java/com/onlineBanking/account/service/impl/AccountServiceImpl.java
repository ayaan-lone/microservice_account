package com.onlineBanking.account.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.onlineBanking.account.client.CardClientHandler;
import com.onlineBanking.account.client.MetadataClientHandler;
import com.onlineBanking.account.client.UserClientHandler;
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

	@Value("${onlineBanking.metadata_service.url}")
	private String metadataServiceUrl;

	@Value("${onlineBanking.card_service.url}")
	private String cardServiceUrl;

	@Value("${onlineBanking.userVerified.url}")
	public String isUserVerifiedUrl;

	private final RestTemplate restTemplate;
	private final AccountRepository accountRepository;
	private final UserClientHandler userClientHandler;
	private final CardClientHandler cardClientHandler;
	private final MetadataClientHandler metadataClientHandler;

	@Autowired
	public AccountServiceImpl(RestTemplate restTemplate, AccountRepository accountRepository,
			UserClientHandler userClientHandler, CardClientHandler cardClientHandler,
			MetadataClientHandler metadataClientHandler) {
		this.restTemplate = restTemplate;
		this.accountRepository = accountRepository;
		this.userClientHandler = userClientHandler;
		this.cardClientHandler = cardClientHandler;
		this.metadataClientHandler = metadataClientHandler;
	}

	private Account isAccountPersists(Long userId) throws AccountApplicationException {
		Optional<Account> accountOptional = accountRepository.findByUserId(userId);

		// If Account does not exist
		if (!accountOptional.isPresent()) {
			throw new AccountApplicationException(HttpStatus.NOT_FOUND, ConstantUtils.ACCOUNT_NOT_FOUND + userId);
		}

		return accountOptional.get();
	}

	public Long generateAccountNumberUtil() {
		Long accountNumber;
		do {
			accountNumber = (long) (Math.random() * 9000000000L) + 1000000000L;
		} while (accountRepository.existsByAccountNo(accountNumber));
		return accountNumber;
	}

	@Override
	public void createAccountWithCard(CreateAccountRequestDto createAccountRequestDto)
			throws AccountApplicationException {

		if (userClientHandler.isUserVerified(createAccountRequestDto.getUserId()) == null) {
			throw new AccountApplicationException(HttpStatus.NOT_FOUND, ConstantUtils.USER_NOT_FOUND);
		}

		// Save account
		Account account = new Account();
		account.setUserId(createAccountRequestDto.getUserId());

		// Retrieve account type from metadata microservice
		String accountType = metadataClientHandler.fetchAccountTypeFromMetadata(createAccountRequestDto.getAccountId());
		account.setAccountType(accountType);
		account.setBalance(0.0);

		// For Unique Account Number
		account.setAccountNo(generateAccountNumberUtil());

		CreateCardRequestDto request = new CreateCardRequestDto(createAccountRequestDto.getUserId(),
				createAccountRequestDto.getAccountId(), createAccountRequestDto.getCardId());
		cardClientHandler.createCard(request);

		accountRepository.save(account);

	}


	// Fetch Account Detail by userId
	@Override
	public Account findAccountByUserId(long userId) throws AccountApplicationException {
		// TODO Auto-generated method stub
		return isAccountPersists(userId);
	}

	@Override
	public Double getAccountBalance(long userId) throws AccountApplicationException {

		Account account = isAccountPersists(userId);
		return account.getBalance();
	}

	// BALANCE UPDATE CHECK
	@Override
	public String updateBalance(UpdateBalanceRequestDto updateBalanceRequestDto) throws AccountApplicationException {

		Account account = isAccountPersists(updateBalanceRequestDto.getUserId());

		if (account != null) {

			if (updateBalanceRequestDto.getTransactionType().equals(TransactionType.CREDIT)) {
				account.setBalance(account.getBalance() + updateBalanceRequestDto.getAmount());
			}

			if (updateBalanceRequestDto.getTransactionType().equals(TransactionType.DEBIT)) {

				if (account.getBalance() <= 0) {
					throw new AccountApplicationException(HttpStatus.BAD_REQUEST, ConstantUtils.BALANCE_NOT_AVAILABLE);
				}

				account.setBalance(account.getBalance() - updateBalanceRequestDto.getAmount());
			}

			accountRepository.save(account);
			return "Balance has been updated";
		}

		throw new AccountApplicationException(HttpStatus.NOT_FOUND, ConstantUtils.ACCOUNT_NOT_FOUND);
	}

}
