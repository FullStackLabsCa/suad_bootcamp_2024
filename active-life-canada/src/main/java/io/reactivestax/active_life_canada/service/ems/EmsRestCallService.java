//package io.reactivestax.active_life_canada.service.ems;
//
//import io.reactivestax.active_life_canada.controller.RestApiClientController;
//import io.reactivestax.active_life_canada.dto.ems.OtpDTO;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
//import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
//import org.springframework.security.oauth2.core.OAuth2AccessToken;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Objects;
//
//public class EmsRestCallService {
//
//    private static final Logger log = LoggerFactory.getLogger(RestApiClientController.class);
//
////    private String EmsBaseUri = "https://localhost:8081/api/v1/ems";
//
//    @Value("${spring.resource-uri:https://localhost:8081/api/v1/ems}")
//    private String resourceUri;
//
//
//    public void sendOTP(OtpDTO otpDTO, String type) {
//        String url = "";
//        if (type.equalsIgnoreCase("email")) {
//            url = resourceUri + "/email";
//        } else if (type.equalsIgnoreCase("phone")) {
//            url = resourceUri + "/phone";
//        } else {
//            url = resourceUri + "/sms";
//        }
//
//        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient("okta", principalName);
//
//        restTemplateCallWithTokenInjection(OAuth2AuthorizedClient ot)
//    }
//
//
//    public Map<String, String> restTemplateCallWithTokenInjection(@RegisteredOAuth2AuthorizedClient("okta") OAuth2AuthorizedClient authorizedClient) {
//        OAuth2AccessToken accessToken = Objects.requireNonNull(authorizedClient).getAccessToken();
//        log.info("Issued: {}, Expires {} ", accessToken.getIssuedAt().toString(), accessToken.getExpiresAt().toString());
//        log.info("Scopes: {} ", accessToken.getScopes().toString());
//        log.info("Token: {} ", accessToken.getTokenValue());
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + accessToken.getTokenValue());
//        HttpEntity request = new HttpEntity(headers);
//
//        // Make the actual HTTP GET request
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> response = restTemplate.exchange(resourceUri, HttpMethod.GET, request, String.class);
//
//        Map<String, String> model = new HashMap<>();
//        model.put("apiResponse", response.getBody());
//        model.put("oauthGrantType", "client credentials flow");
//        model.put("accessToken", authorizedClient.getAccessToken().getTokenValue());
//        return model;
//    }
//
//}
