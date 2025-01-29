package io.reactivestax.active_life_canada.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
public class FamilyGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long familyGroupId;
    private String familyPin;
    private Integer credits;
    private String status;
    private Integer failedLoginAttempts;

    @OneToMany(mappedBy = "familyGroup", cascade = CascadeType.ALL)
    @JsonBackReference
    @ToString.Exclude
    private List<FamilyMember> familyMember;

    @Column(name = "created_ts")
    private LocalDateTime createdTimeStamps;
    @Column(name = "last_updated_ts")
    private LocalDateTime lastUpdatedTimeStamps;
    private Long createdBy;
    private Long updatedBy;

}
