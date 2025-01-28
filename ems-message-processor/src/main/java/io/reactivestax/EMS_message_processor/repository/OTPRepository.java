package io.reactivestax.EMS_message_processor.repository;

import io.reactivestax.EMS_message_processor.domain.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OTPRepository extends JpaRepository<Otp, Long> {

}
