package io.reactivestax.active_life_canada.service;

import io.reactivestax.active_life_canada.dto.ems.EmailDTO;
import io.reactivestax.active_life_canada.dto.ems.OtpDTO;
import io.reactivestax.active_life_canada.dto.ems.PhoneDTO;
import io.reactivestax.active_life_canada.dto.ems.SmsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@Slf4j
public class EmsOtpRestCallService {

    @Autowired
    private RestTemplate restTemplate;


    private static final String EMS_BASE_URL = "http://localhost:8081/api/v1/otp";

    public void sendOTP(OtpDTO otpDTO, String type) {
        String url = "";
        if (type.equalsIgnoreCase("email")) {
            url = EMS_BASE_URL + "/email";
        } else if (type.equalsIgnoreCase("phone")) {
            url = EMS_BASE_URL + "/phone";
        } else {
            url = EMS_BASE_URL + "/sms";
        }

        HttpHeaders headers = createAuthHeaders();
        HttpEntity<OtpDTO> requestEntity = new HttpEntity<>(otpDTO, headers);
        ResponseEntity<OtpDTO> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                OtpDTO.class
        );
        responseEntity.getBody();
    }


    private HttpHeaders createAuthHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        return httpHeaders;
    }
}
