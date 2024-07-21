package com.onlineBanking.account.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.onlineBanking.account.request.CreateCardRequestDto;

@Component
public class CardClientHandler {
	
    private final RestTemplate restTemplate;
    
    @Value("${onlineBanking.card_service.url}")
    private String cardServiceUrl;

    @Autowired
    public CardClientHandler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public void createCard(CreateCardRequestDto createCardRequestDto) {
        HttpEntity<CreateCardRequestDto> httpEntity = new HttpEntity<>(createCardRequestDto);

        // Send the DTO to our restTemplate to create a card
        restTemplate.exchange(cardServiceUrl, HttpMethod.POST, httpEntity, String.class);
    }
}
