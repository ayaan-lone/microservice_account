package com.onlineBanking.account.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
//    public Boolean isUserVerified(Long userId) {
//    	
//        ResponseEntity<Boolean> response = restTemplate.exchange(
//        		isUserVerifiedUrl + "/verify-user?userId=" + userId, HttpMethod.GET, null, Boolean.class);
//        
//        
//        return response.getBody();
//    }
    
    public Boolean isUserVerified(Long userId) {
        // Create an instance of RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Create HttpHeaders and set the Authorization header with the Bearer token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJlbWFpbElkIjoiam9uc25vdyIsInN1YiI6ImpvbnNub3ciLCJpYXQiOjE3MjI0MTUyMDYsImV4cCI6MTcyMjQxNTgwNn0.yBsJtUD20QzH3nUJn3q0SMrM4ussdlDNHg2XqwqhN5o");

        // Create an HttpEntity with the headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Define the URL with the userId parameter
        String url = isUserVerifiedUrl + "/verify-user?userId=" + userId;

        // Make the HTTP GET request with headers
        ResponseEntity<Boolean> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                entity, 
                Boolean.class
        );
        
        

        // Return the response body
        return response.getBody();
    }
}
