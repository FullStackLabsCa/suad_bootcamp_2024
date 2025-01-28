package io.reactivestax.active_life_canada.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

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
    @Column(name = "created_ts")
    private LocalDateTime createdTimeStamps;
    @Column(name = "last_updated_ts")
    private LocalDateTime lastUpdatedTimeStamps;
    private Long createdBy;
    private Long updatedBy;
    @OneToMany
    private FamilyGroup familyGroup;
}
