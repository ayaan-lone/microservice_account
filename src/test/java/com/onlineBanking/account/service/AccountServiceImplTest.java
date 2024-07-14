package com.onlineBanking.account.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.onlineBanking.account.dao.AccountRepository;
import com.onlineBanking.account.entity.Account;
import com.onlineBanking.account.exception.AccountApplicationException;
import com.onlineBanking.account.request.CreateCardDto;
import com.onlineBanking.account.service.impl.AccountServiceImpl;
import com.onlineBanking.account.utils.ConstantUtils;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private AccountRepository accountRepository;

	@InjectMocks
	private AccountServiceImpl accountServiceImpl;

	private Account account;

	@BeforeEach
	void setUp() {
		account = new Account();
		account.setId(1L);
		account.setUserId(123L);
		account.setAccountType("Savings");
		account.setBalance(0);
		account.setAccountNo(Math.abs(new Random().nextLong() % 10000000000000000L));
	}

	@Test
	void testCreateAccountWithCard_Success() throws AccountApplicationException {
		long userId = 123L;
		long accountId = 1L;
		String metadataResponse = "Savings";
		CreateCardDto requestDto = new CreateCardDto(userId, accountId);
		HttpEntity<CreateCardDto> httpEntity = new HttpEntity<>(requestDto);

		when(restTemplate.getForEntity(ConstantUtils.METADATA_SERVICE_URL + accountId, String.class))
				.thenReturn(new ResponseEntity<>(metadataResponse, HttpStatus.OK));
		when(accountRepository.save(any(Account.class))).thenReturn(account);
		doNothing().when(restTemplate).exchange(ConstantUtils.CARD_SERVICE_URL, HttpMethod.POST, httpEntity,
				Object.class);

		accountServiceImpl.createAccountWithCard(userId, accountId);

		verify(restTemplate).getForEntity(ConstantUtils.METADATA_SERVICE_URL + accountId, String.class);
		verify(accountRepository).save(any(Account.class));
		verify(restTemplate).exchange(ConstantUtils.CARD_SERVICE_URL, HttpMethod.POST, httpEntity, Object.class);
	}

	@Test
	void testCreateAccountWithCard_MetadataServiceNotFound() {
		long userId = 123L;
		long accountId = 1L;

		when(restTemplate.getForEntity(ConstantUtils.METADATA_SERVICE_URL + accountId, String.class))
				.thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

		AccountApplicationException exception = assertThrows(AccountApplicationException.class,
				() -> accountServiceImpl.createAccountWithCard(userId, accountId));

		assertEquals(ConstantUtils.ACCOUNT_NOT_FOUND, exception.getMessage());
	}

	@Test
	void testCreateAccountWithCard_AccountRepositoryFailure() {
		long userId = 123L;
		long accountId = 1L;
		String metadataResponse = "Savings";
		CreateCardDto requestDto = new CreateCardDto(userId, accountId);
		HttpEntity<CreateCardDto> httpEntity = new HttpEntity<>(requestDto);

		when(restTemplate.getForEntity(ConstantUtils.METADATA_SERVICE_URL + accountId, String.class))
				.thenReturn(new ResponseEntity<>(metadataResponse, HttpStatus.OK));
		when(accountRepository.save(any(Account.class))).thenThrow(new RuntimeException("Database error"));

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> accountServiceImpl.createAccountWithCard(userId, accountId));

		assertEquals("Database error", exception.getMessage());
		verify(restTemplate).getForEntity(ConstantUtils.METADATA_SERVICE_URL + accountId, String.class);
		verify(accountRepository).save(any(Account.class));
	}

	@Test
	void testCreateAccountWithCard_RestTemplateFailure() {
		long userId = 123L;
		long accountId = 1L;
		String metadataResponse = "Savings";
		CreateCardDto requestDto = new CreateCardDto(userId, accountId);
		HttpEntity<CreateCardDto> httpEntity = new HttpEntity<>(requestDto);

		when(restTemplate.getForEntity(ConstantUtils.METADATA_SERVICE_URL + accountId, String.class))
				.thenReturn(new ResponseEntity<>(metadataResponse, HttpStatus.OK));
		doThrow(new RestClientException("REST template error")).when(restTemplate)
				.exchange(ConstantUtils.CARD_SERVICE_URL, HttpMethod.POST, httpEntity, Object.class);

		RestClientException exception = assertThrows(RestClientException.class,
				() -> accountServiceImpl.createAccountWithCard(userId, accountId));

		assertEquals("REST template error", exception.getMessage());
		verify(restTemplate).getForEntity(ConstantUtils.METADATA_SERVICE_URL + accountId, String.class);
		verify(accountRepository).save(any(Account.class));
		verify(restTemplate).exchange(ConstantUtils.CARD_SERVICE_URL, HttpMethod.POST, httpEntity, Object.class);
	}
}
