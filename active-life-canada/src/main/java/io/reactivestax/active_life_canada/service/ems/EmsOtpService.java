package io.reactivestax.active_life_canada.service.ems;

import io.reactivestax.active_life_canada.constant.AppConstants;
import io.reactivestax.active_life_canada.dto.ems.OtpDTO;
import io.reactivestax.active_life_canada.enums.Status;
import io.reactivestax.active_life_canada.service.outh.OktaTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;


@Service
@Slf4j
public class EmsOtpService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OktaTokenService oktaTokenService;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    private OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;




    public void sendOTP(OtpDTO otpDTO, String type) {
        String url = AppConstants.EMS_BASE_URL + "/otp/" + (type.equalsIgnoreCase("email") ? "email" :
                type.equalsIgnoreCase("phone") ? "phone" : "sms");

        HttpHeaders headers = createAuthHeaders(type);
        HttpEntity<OtpDTO> requestEntity = new HttpEntity<>(otpDTO, headers);
        ResponseEntity<OtpDTO> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                OtpDTO.class
        );
        responseEntity.getBody();
    }

    public Status verifyOTP(OtpDTO otpDTO) {
        String url = AppConstants.EMS_BASE_URL + "/otp/verify/activeLife" ;
        HttpHeaders headers = createAuthHeaders("sms");
        HttpEntity<OtpDTO> requestEntity = new HttpEntity<>(otpDTO, headers);
        ResponseEntity<Status> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                requestEntity,
                Status.class
        );
        return responseEntity.getBody();
    }

    private HttpHeaders createAuthHeaders(String type) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        httpHeaders.setBearerAuth(oktaTokenService.getOAuthToken());

        return httpHeaders;
    }
}
