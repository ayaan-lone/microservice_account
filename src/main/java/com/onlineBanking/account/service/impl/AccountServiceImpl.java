package com.onlineBanking.account.service.impl;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.onlineBanking.account.client.CardClientHandler;
import com.onlineBanking.account.client.MetadataClientHandler;
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

	private final AccountRepository accountRepository;
	private final CardClientHandler cardClientHandler;
	private final MetadataClientHandler metadataClientHandler;

	// Use ConcurrentHashMap for thread-safe lock management
	private final ConcurrentHashMap<Long, Lock> locks = new ConcurrentHashMap<>();

	@Autowired
	public AccountServiceImpl(RestTemplate restTemplate, AccountRepository accountRepository,
		 CardClientHandler cardClientHandler,
			MetadataClientHandler metadataClientHandler) {

		this.accountRepository = accountRepository;
		this.cardClientHandler = cardClientHandler;
		this.metadataClientHandler = metadataClientHandler;
	}

	private Long generateAccountNumberUtil() {
		Long accountNumber;
		do {
			accountNumber = (long) (Math.random() * 9000000000L) + 1000000000L;
		} while (accountRepository.existsByAccountNo(accountNumber));
		return accountNumber;
	}

	@Override
	public String createAccountWithCard(CreateAccountRequestDto createAccountRequestDto, String token, Long userId)
			throws AccountApplicationException {
				
		Optional<Account> accountOptional = accountRepository.findByUserId(userId);
          
		if (accountOptional.isPresent()) {
			throw new AccountApplicationException(HttpStatus.BAD_REQUEST, ConstantUtils.ACCOUNT_ALREADY_EXISTS);
		}
		
		Account account = new Account();
		account.setUserId(userId);
		String accountType = metadataClientHandler.fetchAccountTypeFromMetadata(createAccountRequestDto.getAccountId());
		account.setAccountType(accountType);
		account.setBalance(0.0);
		account.setAccountNo(generateAccountNumberUtil());
		
		CreateCardRequestDto cardDto = new CreateCardRequestDto(userId,
				createAccountRequestDto.getAccountId(), createAccountRequestDto.getCardId());
		cardClientHandler.createCard(cardDto, token);
		
		accountRepository.save(account);	
		return ConstantUtils.ACCOUNT_CREATED;
	}

	@Override
	public Account findAccountByUserId(long userId) throws AccountApplicationException {
		Optional<Account> accountOptional = accountRepository.findByUserId(userId);

		if (!accountOptional.isPresent()) {
			throw new AccountApplicationException(HttpStatus.NOT_FOUND, ConstantUtils.ACCOUNT_NOT_FOUND + userId);
		}

		return accountOptional.get();
	}

	@Override
	public Double getAccountBalance(long userId) throws AccountApplicationException {
		Account account = findAccountByUserId(userId);
		return account.getBalance();
	}

	@Override
	public String updateBalance(UpdateBalanceRequestDto updateBalanceRequestDto, Long userId) throws AccountApplicationException {
//		long userId = updateBalanceRequestDto.getUserId();
//		Lock lock = locks.computeIfAbsent(userId, id -> new ReentrantLock());
//		lock.lock();
		
			Account account = findAccountByUserId(userId);

			if (account != null) {
				if (updateBalanceRequestDto.getTransactionType().equals(TransactionType.CREDIT)) {
					System.out.println("Line number 115");
					
					
					account.setBalance(account.getBalance() + updateBalanceRequestDto.getAmount());
				}
//				 else if (updateBalanceRequestDto.getTransactionType().equals(TransactionType.DEBIT)) {
//					
//					if (account.getBalance() < updateBalanceRequestDto.getAmount()) {
//						System.out.println("Line number 122.");
//						throw new AccountApplicationException(HttpStatus.BAD_REQUEST,
//								ConstantUtils.BALANCE_NOT_AVAILABLE);
//					}
//					System.out.println("The Balance has been Updated ! ");
//					account.setBalance(account.getBalance() - updateBalanceRequestDto.getAmount());
//				}

				accountRepository.save(account);
				return ConstantUtils.BALANCE_UPDATED;
			}

//			throw new AccountApplicationException(HttpStatus.NOT_FOUND, ConstantUtils.ACCOUNT_NOT_FOUND);
			return("Balance is Updated ...");
		
		
//		} finally {
//			lock.unlock();  // Ensure that the lock is always released
//		}
	}
}
