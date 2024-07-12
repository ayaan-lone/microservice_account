package com.onlineBanking.account.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onlineBanking.account.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	boolean existsByAccountNo(Long accountNo);

	Account findByUserId(String userId);

}
