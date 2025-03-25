package io.reactivestax.activelifecanada.repository;

import io.reactivestax.activelifecanada.domain.LoginRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRequestRepository extends JpaRepository<LoginRequest, Long> {
}
