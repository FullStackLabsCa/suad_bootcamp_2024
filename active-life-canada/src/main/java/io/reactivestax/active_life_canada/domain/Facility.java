package io.reactivestax.active_life_canada.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long facilityId;
    private String name;
    private String streetNumber;
    private String streetName;
    private String city;
    private String province;
    private String country;
    private String postalCode;
    private String description;


    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @ToString.Exclude
    private List<OfferedCourse> offeredCourses = new ArrayList<>();


    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL)
    private List<FacilityFunction> facilityFunctions = new ArrayList<>();

    @Column(name = "created_ts")
    private LocalDateTime createdTimeStamp;
    @Column(name = "last_updated_ts")
    private LocalDateTime lastUpdatedTimeStamp;
    private  Long createdBy;
    private  Long lastUpdatedBy;


}
