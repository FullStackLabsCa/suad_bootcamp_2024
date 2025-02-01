package io.reactivestax.active_life_canada.repository;

import io.reactivestax.active_life_canada.domain.SignUpRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface SignUpRequestRepository extends JpaRepository<SignUpRequest, Long> {
//    SignUpRequest findByUuidToken(UUID uuid);
   SignUpRequest findBySignUpRequestIdAndUuidToken(Long signUpRequestId,UUID uuidToken);

//   SignUpRequest findBySignUpRequestIdAndUuidToken(Long signUpRequestId, String uuidToken);

   SignUpRequest findByFamilyMemberIdAndUuidToken(Long familyMemberId, UUID uuidToken);


//   @Query("SELECT s FROM SignUpRequest s WHERE s.signUpRequestId = :signUpRequestId AND s.uuidToken = :uuidToken")
//   SignUpRequest findByIdAndToken(@Param("signUpRequestId") Long signUpRequestId, @Param("uuidToken") UUID uuidToken);


}

