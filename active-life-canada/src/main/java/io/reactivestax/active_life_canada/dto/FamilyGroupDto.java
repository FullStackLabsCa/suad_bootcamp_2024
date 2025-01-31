package io.reactivestax.active_life_canada.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.reactivestax.active_life_canada.domain.FamilyMember;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class FamilyGroupDto {
    private Long familyGroupId;
    private String familyPin;
    private Integer credits;
    private String status;
    private String groupOwner;
    private Integer failedLoginAttempts;
    private List<Long> familyMemberIds;
}
