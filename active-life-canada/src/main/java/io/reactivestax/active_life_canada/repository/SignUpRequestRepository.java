package io.reactivestax.active_life_canada.repository;

import io.reactivestax.active_life_canada.domain.SignUpRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SignUpRequestRepository extends JpaRepository<SignUpRequest, Long> {
   SignUpRequest findByFamilyMemberIdAndUuidToken(Long familyMemberId, UUID uuidToken);

}

