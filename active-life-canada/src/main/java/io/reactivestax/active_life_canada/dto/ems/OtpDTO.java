package io.reactivestax.active_life_canada.dto.ems;

import io.reactivestax.active_life_canada.enums.Status;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class OtpDTO {
    private Long otpId;
    private String validOtp;
    private LocalDateTime createdAt;
    private LocalDateTime lastAccessed;

    private List<LocalDateTime> generationTimeStamps = new ArrayList<>();

    //    @Max(value = 3, message = "Max attempt for OTP validation is 3")
    private Integer validationRetryCount;
    private Status otpStatus;
    private Status verificationStatus;
    private String phone;
    private String email;
    private Boolean isLocked;
    //    @NotNull(message = "Client Id is needed")
    private Long clientId;
}