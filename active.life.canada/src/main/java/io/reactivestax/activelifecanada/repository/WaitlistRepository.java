package io.reactivestax.activelifecanada.repository;

import io.reactivestax.activelifecanada.domain.WaitList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitlistRepository extends JpaRepository<WaitList, Long> {
}
