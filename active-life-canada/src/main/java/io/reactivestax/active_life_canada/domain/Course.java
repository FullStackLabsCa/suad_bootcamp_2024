package io.reactivestax.active_life_canada.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;
    private String name;
    private String description;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @JsonManagedReference
    @ToString.Exclude
    private List<OfferedCourse> offeredCourse;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    @ToString.Exclude
    @JoinColumn(name = "sub_category_id", nullable = false)
    private SubCategory subCategory;

    @OneToMany(mappedBy = "course")
    private List<AgeGroup> ageGroup;

    @Column(name = "created_ts")
    private LocalDateTime createdTimeStamp;

    @Column(name = "last_updated_ts")
    private LocalDateTime lastUpdateTimeStamp;

    private Long createdBy;
    private Long lastUpdatedBy;
}
