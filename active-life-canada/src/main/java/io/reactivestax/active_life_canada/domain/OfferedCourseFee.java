package io.reactivestax.active_life_canada.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.reactivestax.active_life_canada.enums.FeeType;
import jakarta.persistence.*;
import lombok.*;

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

    @Enumerated(EnumType.STRING)
    private FeeType feeType;
    private Double courseFee;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "offered_course_id")
    @ToString.Exclude
    private OfferedCourse offeredCourse;

    @Column(name = "created_ts")
    private LocalDateTime createdTimeStamp = LocalDateTime.now();

    @Column(name = "last_updated_ts")
    private LocalDateTime lastUpdateTimeStamp = LocalDateTime.now();
    private Long createdBy;
    private Long lastUpdatedBy;
}
