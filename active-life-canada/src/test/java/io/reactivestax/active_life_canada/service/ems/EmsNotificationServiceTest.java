package io.reactivestax.active_life_canada.service.ems;


import io.reactivestax.active_life_canada.dto.ems.EmailDTO;
import io.reactivestax.active_life_canada.dto.ems.PhoneDTO;
import io.reactivestax.active_life_canada.dto.ems.SmsDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@RestClientTest(EmsNotificationService.class)
class EmsNotificationServiceTest {

    @Autowired
    private EmsNotificationService emsNotificationService;

    @MockitoBean
    private RestTemplate restTemplate;

    @Test
    void testSendEmailSignUpNotification() {
        EmailDTO emailDTO = new EmailDTO();
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(),
                eq(EmailDTO.class)))
                .thenReturn(new ResponseEntity<>(emailDTO, HttpStatus.OK));

        emsNotificationService.sendEmailSignUpNotification(emailDTO);
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(), eq(EmailDTO.class));
    }

    @Test
    void testSendPhoneNotification() {
        PhoneDTO phoneDTO = new PhoneDTO();
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(),
                eq(PhoneDTO.class)))
                .thenReturn(new ResponseEntity<>(phoneDTO, HttpStatus.OK));

        emsNotificationService.sendPhoneNotification(phoneDTO);
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(), eq(PhoneDTO.class));
    }

    @Test
    void testSendSmsNotification() {
        SmsDTO smsDTO = new SmsDTO();
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(),
                eq(SmsDTO.class)))
                .thenReturn(new ResponseEntity<>(smsDTO, HttpStatus.OK));

        emsNotificationService.sendSmsNotification(smsDTO);
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(), eq(SmsDTO.class));
    }
}
