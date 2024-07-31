package com.onlineBanking.account.request;

public class CreateAccountRequestDto {

//	@NotEmpty
	private long accountId;

//	@NotEmpty
	private long cardId;

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
