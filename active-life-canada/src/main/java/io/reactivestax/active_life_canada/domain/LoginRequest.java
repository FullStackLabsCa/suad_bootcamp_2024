package io.reactivestax.active_life_canada.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
public class LoginRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loginRequestId;
    @Column(name = "created_ts")
    private LocalDateTime createdTimeStamp;
    @OneToOne
    private FamilyMember familyMember;
}
