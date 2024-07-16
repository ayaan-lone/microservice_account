package com.onlineBanking.account.util;

public class ConstantUtils {
	public static final String CARD_SERVICE_URL = "http://localhost:8082/api/v1/create";
	public static final String METADATA_SERVICE_URL ="http://localhost:8084/api/v1/accountType/" ;
	public static final String ACCOUNT_NOT_FOUND = "Failed to fetch account type from metadata microservice";
	public static final String INVALID_TRANSACTION = "Invalid Transaction";
	public static final String BALANCE_NOT_AVAILABLE = "Enough balance is not available to complete the transaction"; 
}
