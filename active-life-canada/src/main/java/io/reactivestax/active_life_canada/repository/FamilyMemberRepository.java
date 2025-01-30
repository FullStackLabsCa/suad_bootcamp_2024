package io.reactivestax.active_life_canada.repository;

import io.reactivestax.active_life_canada.domain.FamilyMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Long> {
}
