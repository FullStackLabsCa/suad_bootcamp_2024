package io.reactivestax.activelifecanada.repository;

import io.reactivestax.activelifecanada.domain.FamilyGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyGroupRepository extends JpaRepository<FamilyGroup, Long> {
}
