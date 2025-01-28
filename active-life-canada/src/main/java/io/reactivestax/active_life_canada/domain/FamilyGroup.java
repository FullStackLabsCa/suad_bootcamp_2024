package io.reactivestax.active_life_canada.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
public class FamilyGroup {
    @Id
    @GeneratedValue
    private Long familyGroupId;
}
