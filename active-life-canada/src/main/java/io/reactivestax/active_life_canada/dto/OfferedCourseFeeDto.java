package io.reactivestax.active_life_canada.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.reactivestax.active_life_canada.enums.FeeType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OfferedCourseFeeDto {

    @Enumerated(EnumType.STRING)
    private FeeType feeType;

    private Double courseFee;

}
