package io.reactivestax.active_life_canada.repository;

import io.reactivestax.active_life_canada.domain.WaitList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitlistRepository extends JpaRepository<WaitList, Long> {
}
