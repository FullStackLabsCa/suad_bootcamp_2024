package io.reactivestax.active_life_canada.repository;

import io.reactivestax.active_life_canada.domain.LoginRequest;
import io.reactivestax.active_life_canada.domain.OfferedCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRequestRepository extends JpaRepository<LoginRequest, Long> {
}
