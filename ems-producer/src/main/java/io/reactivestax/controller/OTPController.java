package io.reactivestax.controller;

import io.reactivestax.dto.OtpDTO;
import io.reactivestax.enums.Status;
import io.reactivestax.service.OTPService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/otp")
public class OTPController {

    @Autowired
    private OTPService otpService;

    @PostMapping("/sms")
    public ResponseEntity<OtpDTO> createOtpForSms(@Valid @RequestBody OtpDTO otpDTO) {
        return ResponseEntity.ok(otpService.createOtpForSms(otpDTO));
    }

    @PostMapping("/call")
    public ResponseEntity<OtpDTO> createOtpForCall(@Valid @RequestBody OtpDTO otpDTO) {
        return ResponseEntity.ok(otpService.createOtpForPhone(otpDTO));
    }

    @PostMapping("/email")
    public ResponseEntity<OtpDTO> createOtpForEmail(@Valid @RequestBody OtpDTO otpDTO) {
        return ResponseEntity.ok(otpService.createOtpForEmail(otpDTO));
    }

    @PutMapping("/verify")
    public ResponseEntity<OtpDTO> verifyOtp(@Valid @RequestBody OtpDTO otpDTO) {
        return ResponseEntity.ok(otpService.verifyOtp(otpDTO));
    }

    @PutMapping("/verify/activeLife")
    public ResponseEntity<Status> verifyOtpForActiveLife(@Valid @RequestBody OtpDTO otpDTO) {
        return ResponseEntity.ok(otpService.verifyOtpForActiveLife(otpDTO));
    }


    @GetMapping("/status/{clientId}")
    public ResponseEntity<Status> statusForOTP(@Valid @PathVariable Long clientId) {
        return ResponseEntity.ok(otpService.statusForOTP(clientId));
    }

}
