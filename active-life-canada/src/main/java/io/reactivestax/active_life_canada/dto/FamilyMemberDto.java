package io.reactivestax.active_life_canada.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class FamilyMemberDto {
    private Long familyMemberId;
    private String name;
    private String familyPin;
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
    private Boolean isActive;
    private String preferredContact;
    private List<Long> familyCourseRegistrationIds;
    private Long familyGroupId;
    private List<Long> loginRequestIds;
}
