package io.reactivestax.EMS_message_processor.repository;

import io.reactivestax.EMS_message_processor.domain.Sms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsRepository extends JpaRepository<Sms, Long> {
}
