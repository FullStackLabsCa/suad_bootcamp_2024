package io.reactivestax.active_life_canada.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
public class RestApiClientController {

    @Autowired
    RestTemplate restTemplate;

    private static final Logger log = LoggerFactory.getLogger(RestApiClientController.class);

    @Value("${spring.resource-uri:https://localhost:8081/ems/sms}")
    private String resourceUri;


    @GetMapping("api/v1/call-api")
    String restTemplate(@RegisteredOAuth2AuthorizedClient("okta") OAuth2AuthorizedClient authorizedClient) {
        OAuth2AccessToken accessToken = Objects.requireNonNull(authorizedClient).getAccessToken();
        log.info("Issued: {}, Expires {} ", accessToken.getIssuedAt().toString(), accessToken.getExpiresAt().toString());
        log.info("Scopes: {} ", accessToken.getScopes().toString());
        log.info("Token: {} ", accessToken.getTokenValue());
        // Make the actual HTTP GET request
      return accessToken.getTokenValue();
    }
}