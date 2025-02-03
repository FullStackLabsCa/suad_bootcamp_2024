package io.reactivestax.active_life_canada.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FamilyMemberDto {
//    private Long familyMemberId;
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
    private String preferredContact;
}
