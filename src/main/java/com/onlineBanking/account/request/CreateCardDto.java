package com.onlineBanking.account.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateCardDto {

	@JsonProperty("userId")
	private long userId;

	@JsonProperty("accountId")
	private long accountId;

	public CreateCardDto(long userId, long accountId) {
		this.userId = userId;
		this.accountId = accountId;
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
