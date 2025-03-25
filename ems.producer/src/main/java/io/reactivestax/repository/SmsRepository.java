package io.reactivestax.repository;

import io.reactivestax.domain.Sms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsRepository extends JpaRepository<Sms, Long> {
}
