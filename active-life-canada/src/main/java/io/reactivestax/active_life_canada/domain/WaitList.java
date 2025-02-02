package io.reactivestax.active_life_canada.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OfferedCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long offeredCourseId;
    private String barCode;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer numberOfClassesOffered;
    private Integer numberOfSeats;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean isAllDayCourse;
    private LocalDate registrationStartDate;
    private Boolean availableForEnrollment;


    @OneToMany(mappedBy = "offeredCourse", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @ToString.Exclude
    private List<OfferedCourseFee> offeredCourseFees = new ArrayList<>();


    @OneToMany(mappedBy = "offeredCourse", cascade = CascadeType.MERGE, orphanRemoval = true)
    @JsonManagedReference
    @ToString.Exclude
    private List<CourseRegistration> courseRegistrations = new ArrayList<>();

    @ManyToOne
    @JsonBackReference
    @ToString.Exclude
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JsonBackReference
    @ToString.Exclude
    @JoinColumn(name = "facility_id")
    private  Facility facility;

    @Column(name = "created_ts")
    private LocalDateTime createdTimeStamp;

    @Column(name = "last_updated_ts")
    private LocalDateTime lastUpdatedTimeStamp;

    private  Long createdBy;
}
