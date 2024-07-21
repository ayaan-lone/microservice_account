//package com.onlineBanking.account.service;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.client.RestTemplate;
//
//import com.onlineBanking.account.dao.AccountRepository;
//import com.onlineBanking.account.entity.Account;
//import com.onlineBanking.account.exception.AccountApplicationException;
//import com.onlineBanking.account.request.BalanceDto;
//import com.onlineBanking.account.service.impl.AccountServiceImpl;
//import com.onlineBanking.account.util.ConstantUtils;
//
//@ExtendWith(MockitoExtension.class)
//public class AccountServiceImplTest {
//
//    @InjectMocks
//    private AccountServiceImpl accountServiceImpl;
//
//    @Mock
//    private AccountRepository accountRepository;
//
//    @Mock
//    private RestTemplate restTemplate;
//
//    private BalanceDto balanceDto;
//
//    @BeforeEach
//    public void setUp() {
//        balanceDto = new BalanceDto();
//        balanceDto.setUserId(1L);
//        balanceDto.setAmount(100);
//        balanceDto.setTransactionType("credit");
//
//        accountServiceImpl = new AccountServiceImpl();
//        accountServiceImpl.accountRepository = accountRepository;
//        accountServiceImpl.restTemplate = restTemplate;
//    }
//
//    @Test
//    public void testCreateAccountWithCard_Success() throws AccountApplicationException {
//        when(restTemplate.getForEntity(any(String.class), eq(String.class)))
//            .thenReturn(new ResponseEntity<>("SAVINGS", HttpStatus.OK));
//        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
//        ResponseEntity<Object> responseEntity = new ResponseEntity<>(HttpStatus.OK);
//        when(restTemplate.exchange(eq(ConstantUtils.CARD_SERVICE_URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object.class)))
//            .thenReturn(responseEntity);
//
//        accountServiceImpl.createAccountWithCard(1L, 1001L);
//
//        verify(accountRepository).save(any(Account.class));
//        verify(restTemplate).exchange(eq(ConstantUtils.CARD_SERVICE_URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object.class));
//    }
//
//    @Test
//    public void testCreateAccountWithCard_MetadataServiceFail() {
//        when(restTemplate.getForEntity(any(String.class), eq(String.class)))
//            .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));
//
//        AccountApplicationException exception = assertThrows(AccountApplicationException.class, () -> {
//            accountServiceImpl.createAccountWithCard(1L, 1001L);
//        });
//
//        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
//        assertEquals(ConstantUtils.ACCOUNT_NOT_FOUND, exception.getMessage());
//    }
//
//    @Test
//    public void testUpdateAccountBalance_Success_Credit() throws AccountApplicationException {
//        Account account = new Account();
//        account.setBalance(200);
//        when(accountRepository.findByUserId(balanceDto.getUserId())).thenReturn(account);
//
//        String response = accountServiceImpl.updateAccountBalance(balanceDto);
//
//        assertEquals("Balance have been updated", response);
//        assertEquals(300.0, account.getBalance());
//        verify(accountRepository).save(account);
//    }
//
//    @Test
//    public void testUpdateAccountBalance_Success_Debit() throws AccountApplicationException {
//        balanceDto.setTransactionType("debit");
//        Account account = new Account();
//        account.setBalance(200);
//        when(accountRepository.findByUserId(balanceDto.getUserId())).thenReturn(account);
//
//        String response = accountServiceImpl.updateAccountBalance(balanceDto);
// 
//        assertEquals("Balance have been updated", response);
//        assertEquals(100.0, account.getBalance());
//        verify(accountRepository).save(account);
//    }
//
//    @Test
//    public void testUpdateAccountBalance_InvalidTransaction() {
//        balanceDto.setTransactionType("invalid");
//
//        AccountApplicationException exception = assertThrows(AccountApplicationException.class, () -> {
//            accountServiceImpl.updateAccountBalance(balanceDto);
//        });
//
//        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
//        assertEquals(ConstantUtils.INVALID_TRANSACTION, exception.getMessage());
//    }
//    
//    @Test
//    public void testUpdateAccountBalance_InsufficientBalance() {
//        balanceDto.setTransactionType("debit");
//        balanceDto.setAmount(300);
//        Account account = new Account();
//        account.setBalance(200);
//        when(accountRepository.findByUserId(balanceDto.getUserId())).thenReturn(account);
//
//        AccountApplicationException exception = assertThrows(AccountApplicationException.class, () -> {
//            accountServiceImpl.updateAccountBalance(balanceDto);
//        });
//
//        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
//        assertEquals(ConstantUtils.BALANCE_NOT_AVAILABLE, exception.getMessage());
//    }
//}
