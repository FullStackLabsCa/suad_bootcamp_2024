package io.reactivestax.activelifecanada.repository;

import io.reactivestax.activelifecanada.domain.SignUpRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SignUpRequestRepository extends JpaRepository<SignUpRequest, Long> {
   SignUpRequest findByFamilyMemberIdAndUuidToken(Long familyMemberId, UUID uuidToken);

}

