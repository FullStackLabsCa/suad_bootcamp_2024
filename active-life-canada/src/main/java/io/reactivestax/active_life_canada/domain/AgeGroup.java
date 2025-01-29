package io.reactivestax.active_life_canada.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgeGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ageGroupId;
    private String shortCode;
    private String description;
    private LocalDateTime createdTimeStamp;
    private LocalDateTime lastUpdatedTimeStamp;
    private Long createdBy;
    private Long lastUpdatedBy;

    @ManyToOne
    @JsonManagedReference
    @ToString.Exclude
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

}
