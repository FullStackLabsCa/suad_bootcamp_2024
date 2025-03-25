package io.reactivestax.EMS_message_processor.repository;

import io.reactivestax.EMS_message_processor.domain.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Long> {
}
