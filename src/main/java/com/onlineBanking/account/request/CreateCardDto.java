package com.onlineBanking.account.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateCardDto {

	@JsonProperty("userId")
	private long userId;

	@JsonProperty("accountType")
	private String accountType;

	public CreateCardDto(long userId, String accountType) {
		this.userId = userId;
		this.accountType = accountType;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

}
