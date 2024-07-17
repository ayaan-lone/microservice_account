package com.onlineBanking.account.request;

import jakarta.validation.constraints.NotEmpty;

public class CreateAccountRequestDto {

	@NotEmpty
	private long userId;

	@NotEmpty
	private long accountId;

	@NotEmpty
	private long cardId;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public long getCardId() {
		return cardId;
	}

	public void setCardId(long cardId) {
		this.cardId = cardId;
	}

}
