package io.reactivestax.activelifecanada.repository;

import io.reactivestax.activelifecanada.domain.FamilyMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Long> {
}
