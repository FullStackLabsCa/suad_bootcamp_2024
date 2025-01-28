package io.reactivestax.EMS_message_processor.repository;


import io.reactivestax.EMS_message_processor.domain.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneRepository extends JpaRepository<Phone, Long> {
}
