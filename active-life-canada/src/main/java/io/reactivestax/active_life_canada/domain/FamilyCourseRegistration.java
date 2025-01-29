package io.reactivestax.active_life_canada.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FamilyCourseRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long familyCourseRegistrationId;
    private BigInteger cost;
    private LocalDate enrollmentDate;
    private Boolean isWithdraw;
    private BigInteger withdrawCredits;
    private String enrollmentActor;
    private Long enrollmentActorId;
    private LocalDateTime createdTimeStamp;
    private Long offeredCourseId;
    private Long familyMemberId;

    @Column(name = "last_updated_ts")
    private LocalDateTime lastUpdatedTimeStamp;
    private Long lastUpdatedBy;
}
