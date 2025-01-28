package io.reactivestax.EMS_message_processor.twilio;


import io.reactivestax.EMS_message_processor.domain.Otp;
import io.reactivestax.EMS_message_processor.repository.EmailRepository;
import io.reactivestax.EMS_message_processor.repository.OTPRepository;
import io.reactivestax.EMS_message_processor.repository.PhoneRepository;
import io.reactivestax.EMS_message_processor.repository.SmsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;


@Service
@Slf4j
public class TwilioRestService {

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.account-sid}")
    private String accountSID;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private SmsRepository smsRepository;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private OTPRepository otpRepository;

    private static final String TWILIO_BASE_URL = "https://api.twilio.com/2010-04-01";


    public void callTwilio(String message) {
        Otp otp = otpRepository.findById(Long.parseLong(message)).orElseThrow(() -> new RuntimeException("Phone to send the otp is not found"));
        sendSms(otp.getPhone(), twilioPhoneNumber, otp.getValidOtp());
    }


    public void sendSms(String to, String from, String body) {
        String url = TWILIO_BASE_URL + "/Accounts/" + accountSID + "/Messages.json";
        HttpHeaders headers = createAuthHeaders();
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("To", to);
        formData.add("From", from);
        formData.add("Body", body);

        // Create request entity
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);


        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            // Check response status and log
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("SMS sent successfully! Response: " + response.getBody());
            } else {
                log.debug("Failed to send SMS. Status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error while sending SMS: " + e.getMessage());
        }

    }

    private HttpHeaders createAuthHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        //encoded for the authentication purpose..
        String auth = accountSID + ":" + authToken;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        httpHeaders.set("Authorization", "Basic " + encodedAuth);
        return httpHeaders;
    }
}
