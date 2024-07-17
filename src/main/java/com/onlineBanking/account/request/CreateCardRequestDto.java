package com.onlineBanking.account.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateCardRequestDto {

	@JsonProperty("userId")
	private long userId;

	@JsonProperty("accountId")
	private long accountId;

	@JsonProperty("id")
	private long cardId;

	public CreateCardRequestDto(long userId, long accountId, long cardId) {
		this.userId = userId;
		this.accountId = accountId;
		this.cardId= cardId;
	}

	public long getCardId() {
		return cardId;
	}

	public void setCardId(long cardId) {
		this.cardId = cardId;
	}

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

}
