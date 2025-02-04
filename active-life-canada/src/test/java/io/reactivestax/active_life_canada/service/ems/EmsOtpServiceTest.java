package io.reactivestax.active_life_canada.service.ems;


import io.reactivestax.active_life_canada.dto.ems.OtpDTO;
import io.reactivestax.active_life_canada.enums.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@RestClientTest(EmsOtpService.class)
class EmsOtpServiceTest {

    @Autowired
    private EmsOtpService emsOtpService;

    @MockitoBean
    private RestTemplate restTemplate;

    @Test
    void testSendOTPEmail() {
        OtpDTO otpDTO = new OtpDTO();
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(),
                eq(OtpDTO.class)))
                .thenReturn(new ResponseEntity<>(otpDTO, HttpStatus.OK));

        emsOtpService.sendOTP(otpDTO, "email");
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(), eq(OtpDTO.class));
    }

    @Test
    void testSendOTPPhone() {
        OtpDTO otpDTO = new OtpDTO();
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(),
                eq(OtpDTO.class)))
                .thenReturn(new ResponseEntity<>(otpDTO, HttpStatus.OK));

        emsOtpService.sendOTP(otpDTO, "phone");
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(), eq(OtpDTO.class));
    }

    @Test
    void testSendOTPSms() {
        OtpDTO otpDTO = new OtpDTO();
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(),
                eq(OtpDTO.class)))
                .thenReturn(new ResponseEntity<>(otpDTO, HttpStatus.OK));

        emsOtpService.sendOTP(otpDTO, "sms");
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(), eq(OtpDTO.class));
    }

    @Test
    void testVerifyOTP() {
        OtpDTO otpDTO = new OtpDTO();
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.PUT),
                any(),
                eq(Status.class)))
                .thenReturn(new ResponseEntity<>(Status.VERIFIED, HttpStatus.OK));

        Status status = emsOtpService.verifyOTP(otpDTO);
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.PUT), any(), eq(Status.class));
    }
}
