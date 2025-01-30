package io.reactivestax.active_life_canada.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LoginRequestDto {
    private Long loginRequestId;
    private String familyPin;
    private Long familyMemberId;
    private LocalDateTime createdTimeStamp;
}
