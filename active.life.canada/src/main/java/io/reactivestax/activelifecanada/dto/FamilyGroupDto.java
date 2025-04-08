package io.reactivestax.activelifecanada.dto;

import io.reactivestax.activelifecanada.domain.FamilyMember;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FamilyGroupDto {
    private Long familyGroupId;
    private Integer credits;
    private String status;
    private String groupOwner;
    private Boolean isGroupOwner;
    private Integer failedLoginAttempts;
    private List<FamilyMember> familyMember;
    private Long totalCourseEnrolled;
    private Double totalCostOfEnrolledCourses;
}
