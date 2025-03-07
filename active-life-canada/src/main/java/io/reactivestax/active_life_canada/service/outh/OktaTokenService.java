package io.reactivestax.active_life_canada.service.outh;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class OktaTokenService {


    @Autowired
    private OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;



    //For getting the token using restTemplate
//    public String getAccessToken(String type) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBasicAuth(clientId, clientSecret);
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        String body = "grant_type=client_credentials&scope=ems." + type;
//
//        HttpEntity<String> request = new HttpEntity<>(body, headers);
//        ResponseEntity<Map> response = restTemplate.exchange(tokenUri, HttpMethod.POST, request, Map.class);
//
//        if (response.getStatusCode() == HttpStatus.OK) {
//            return (String) response.getBody().get("access_token");
//        }
//        throw new RuntimeException("Failed to get access token from Okta");
//    }

    //Calling with the security context set by the spring security
//    public String getOktaToken(OAuth2AuthorizedClient authorizedClient) {
//        OAuth2AccessToken accessToken = Objects.requireNonNull(authorizedClient).getAccessToken();
//        log.info("Issued: {}, Expires {} ", accessToken.getIssuedAt().toString(), accessToken.getExpiresAt().toString());
//        log.info("Scopes: {} ", accessToken.getScopes().toString());
//        log.info("Token: {} ", accessToken.getTokenValue());
//        return accessToken.getTokenValue();
//    }

    //Getting the token by OAuthClientManager here we set the bean explicitly
    public String getOAuthToken() {
        OAuth2AuthorizedClient authorize = oAuth2AuthorizedClientManager
                .authorize(OAuth2AuthorizeRequest.withClientRegistrationId("okta")
                .principal("client")
                .build());
        assert authorize != null;
       return authorize.getAccessToken().getTokenValue();
    }


}
