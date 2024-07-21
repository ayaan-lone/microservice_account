package com.onlineBanking.account.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.onlineBanking.account.exception.AccountApplicationException;
import com.onlineBanking.account.util.ConstantUtils;

@Component
public class MetadataClientHandler {

    private final RestTemplate restTemplate;
    @Value("${onlineBanking.metadata_service.url}")
    private String metadataServiceUrl;
  
    @Autowired
    public MetadataClientHandler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String fetchAccountTypeFromMetadata(long accountId) throws AccountApplicationException {
        String metadataUrl = metadataServiceUrl + accountId;
        ResponseEntity<String> response = restTemplate.getForEntity(metadataUrl, String.class);

        if (response == null || !response.getStatusCode().is2xxSuccessful()) {
            throw new AccountApplicationException(HttpStatus.NOT_FOUND, ConstantUtils.ACCOUNT_TYPE_NOT_FOUND);
        }
        return response.getBody();
    }
}
