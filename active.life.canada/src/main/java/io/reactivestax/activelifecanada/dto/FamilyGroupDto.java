package io.reactivestax.activelifecanada.dto;

import lombok.Builder;
import lombok.Data;

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
