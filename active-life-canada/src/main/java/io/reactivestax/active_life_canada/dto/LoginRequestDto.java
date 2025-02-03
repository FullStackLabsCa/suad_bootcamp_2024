package io.reactivestax.active_life_canada.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {
    private Long loginRequestId;
    private Long familyMemberId;
    private String familyPin;
    private String otp;
    private LocalDateTime createdTimeStamp;
}
