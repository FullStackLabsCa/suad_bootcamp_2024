package io.reactivestax.active_life_canada.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OfferedCourseFee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feeId;
    private String feeType;
    private BigInteger courseFee;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "offered_course_id", nullable = false)
    @ToString.Exclude
    private OfferedCourse offeredCourse;

    @Column(name = "created_ts")
    private LocalDateTime createdTimeStamp;

    @Column(name = "last_updated_ts")
    private LocalDateTime lastUpdateTimeStamp;

    private Long createdBy;

    private Long lastUpdatedBy;
}
