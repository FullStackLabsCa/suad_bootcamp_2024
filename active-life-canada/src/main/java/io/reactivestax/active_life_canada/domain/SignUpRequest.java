package io.reactivestax.active_life_canada.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long offeredCourseId;
    private Long signUpRequestTable;
    private UUID uuidToken;
    private Long familyMemberId;
}
