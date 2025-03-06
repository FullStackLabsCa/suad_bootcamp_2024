package io.reactivestax.active_life_canada.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class OktaTokenService {

    @Value("${okta.client-id}")
    private String clientId;

    @Value("${okta.client-secret}")
    private String clientSecret;

    @Value("${okta.token-uri}")
    private String tokenUri;

    private final RestTemplate restTemplate;

    public OktaTokenService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public String getAccessToken(String type) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "grant_type=client_credentials&scope=ems." + type;

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(tokenUri, HttpMethod.POST, request, Map.class);

        if(response.getStatusCode() == HttpStatus.OK){
            return (String) response.getBody().get("access_token");
        }
        throw new RuntimeException("Failed to get access token from Okta");
    }
}
