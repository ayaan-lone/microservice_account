package com.onlineBanking.account.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserClientHandler {

    private final RestTemplate restTemplate;

    @Value("${onlineBanking.userVerified.url}")
    private String isUserVerifiedUrl;

    @Autowired
    public UserClientHandler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Method to check if a user is verified
    public Boolean isUserVerified(Long userId) {
        ResponseEntity<Boolean> response = restTemplate.exchange(
        		isUserVerifiedUrl + "/verify-user?userId=" + userId, HttpMethod.GET, null, Boolean.class);
        return response.getBody();
    }
}
