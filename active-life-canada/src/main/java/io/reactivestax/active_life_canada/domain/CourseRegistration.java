package io.reactivestax.active_life_canada.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

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
    private Double cost;
    private LocalDate enrollmentDate;
    private Boolean isWithdraw;
    private Double withdrawCredits;
    private String enrollmentActor;
    private Long enrollmentActorId;
    private LocalDateTime createdTimeStamp;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    @ToString.Exclude
    @JoinColumn(name = "family_member_id")
    private FamilyMember familyMember;

    @ManyToOne
    @JsonBackReference
    @ToString.Exclude
    @JoinColumn(name = "offered_course_id")
    private OfferedCourse offeredCourse;

    @Column(name = "last_updated_ts")
    private LocalDateTime lastUpdatedTimeStamp;
    private Long lastUpdatedBy;
}
