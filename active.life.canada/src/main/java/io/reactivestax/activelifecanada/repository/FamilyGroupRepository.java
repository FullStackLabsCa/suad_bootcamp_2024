package io.reactivestax.activelifecanada.repository;

import io.reactivestax.activelifecanada.domain.FamilyGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FamilyGroupRepository extends JpaRepository<FamilyGroup, Long> {
    //retrieve the familyGroup on the basis of the family member
    Optional<FamilyGroup> findByFamilyMemberFamilyMemberId(Long familyMemberId);
}
