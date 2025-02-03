package io.reactivestax.active_life_canada.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {
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
    private String preferredContact;
}
