package io.reactivestax.active_life_canada.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FamilyMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "family_member_id")
    private Long familyMemberId;

    private String name;
    private LocalDate dob;
    private String gender;
    private String emailId;
    private String streetNumber;
    private String streetName;
    private String city;
    private String province;
    private String country;
    private String homePhone;
    private String businessPhone;
    private String language;
    private String memberLoginId;
    private Boolean isActive = false;
    private String preferredContact;

    @OneToMany(mappedBy = "familyMember")
    @JsonManagedReference
    private List<FamilyCourseRegistration> familyCourseRegistrations = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    @JoinColumn(name = "family_group_id", nullable = false)
    private FamilyGroup familyGroup;

    @OneToMany(mappedBy = "familyMember")
    @JsonManagedReference
    @ToString.Exclude
    private List<LoginRequest> loginRequests = new ArrayList<>();
}


