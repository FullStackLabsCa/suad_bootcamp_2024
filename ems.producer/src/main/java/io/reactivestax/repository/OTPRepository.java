package io.reactivestax.repository;

import io.reactivestax.domain.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OTPRepository extends JpaRepository<Otp, Long> {
    Otp findOtpByClientId(Long clientId);
    Otp findOtpByValidOtp(String otp);
}
