package io.reactivestax.active_life_canada.domain;

import jakarta.persistence.*;
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
    private Long signUpRequestId;
    @Column(nullable = false, unique = true)
    private UUID uuidToken;
    private Long familyMemberId;
}
