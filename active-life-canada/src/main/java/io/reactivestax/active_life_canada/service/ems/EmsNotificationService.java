package io.reactivestax.active_life_canada.service.ems;

import io.reactivestax.active_life_canada.constant.AppConstants;
import io.reactivestax.active_life_canada.dto.ems.EmailDTO;
import io.reactivestax.active_life_canada.dto.ems.PhoneDTO;
import io.reactivestax.active_life_canada.dto.ems.SmsDTO;
import io.reactivestax.active_life_canada.service.outh.OktaTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;


@Service
@Slf4j
public class EmsNotificationService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OktaTokenService oktaTokenService;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;



    public void sendEmailSignUpNotification(EmailDTO emailDTO) {
        String emailUrl = AppConstants.EMS_BASE_URL + "/email";
        HttpHeaders headers = createAuthHeaders("email");
        HttpEntity<EmailDTO> requestEntity = new HttpEntity<>(emailDTO, headers);
        ResponseEntity<EmailDTO> responseEntity = restTemplate.exchange(
                emailUrl,
                HttpMethod.POST,
                requestEntity,
                EmailDTO.class
        );
        responseEntity.getBody();
    }

    public void sendPhoneNotification(PhoneDTO phoneDTO) {
        String phoneUrl = AppConstants.EMS_BASE_URL + "/phone";
        HttpHeaders headers = createAuthHeaders("phone");
        HttpEntity<PhoneDTO> requestEntity = new HttpEntity<>(phoneDTO, headers);
        ResponseEntity<PhoneDTO> responseEntity = restTemplate.exchange(
                phoneUrl,
                HttpMethod.POST,
                requestEntity,
                PhoneDTO.class
        );

        responseEntity.getBody();
    }


    public void sendSmsNotification(SmsDTO smsDTO) {
        String smsUrl = AppConstants.EMS_BASE_URL + "/sms";
        HttpHeaders headers = createAuthHeaders("sms");
        HttpEntity<SmsDTO> requestEntity = new HttpEntity<>(smsDTO, headers);
        ResponseEntity<SmsDTO> responseEntity = restTemplate.exchange(
                smsUrl,
                HttpMethod.POST,
                requestEntity,
                SmsDTO.class
        );
        responseEntity.getBody();
    }

    private HttpHeaders createAuthHeaders(String type) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        httpHeaders.setBearerAuth(oktaTokenService.getOAuthToken());
        return httpHeaders;
    }
}
