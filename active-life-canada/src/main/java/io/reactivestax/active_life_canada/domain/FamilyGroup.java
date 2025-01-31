package io.reactivestax.active_life_canada.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FamilyGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long familyGroupId;
    private String familyPin;
    private Integer credits = 0;
    private String status = "inActive";
    private String groupOwner;
    private Integer failedLoginAttempts;

    @OneToMany(mappedBy = "familyGroup", cascade = CascadeType.ALL)
    @JsonManagedReference
    @ToString.Exclude
    private List<FamilyMember> familyMember = new ArrayList<>();

    @Column(name = "created_ts")
    private LocalDateTime createdTimeStamps = LocalDateTime.now();

    @Column(name = "last_updated_ts")
    private LocalDateTime lastUpdatedTimeStamps;
    private Long createdBy;
    private Long updatedBy;
}
