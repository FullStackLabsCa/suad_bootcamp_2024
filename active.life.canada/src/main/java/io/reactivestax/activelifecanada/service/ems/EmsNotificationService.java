package io.reactivestax.activelifecanada.service.ems;

import io.reactivestax.activelifecanada.constant.AppConstants;
import io.reactivestax.activelifecanada.dto.ems.EmailDTO;
import io.reactivestax.activelifecanada.dto.ems.PhoneDTO;
import io.reactivestax.activelifecanada.dto.ems.SmsDTO;
import io.reactivestax.activelifecanada.service.outh.OktaTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;


@Service
@Slf4j
public class EmsNotificationService implements Comparable {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OktaTokenService oktaTokenService;


//    List<Integer> test =  Arrays.asList(5,6, 2, 8, 11);
//
//    public int findMax(){
//        Optional<Integer> max = test.stream().max(Integer::compare);
//        return max.get();
//    }




    public final void sendEmailSignUpNotification(EmailDTO emailDTO) {
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

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
