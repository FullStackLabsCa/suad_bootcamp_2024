package io.reactivestax.activelifecanada.dto;

import io.reactivestax.activelifecanada.enums.FeeType;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OfferedCourseFeeDto {

    @Enumerated(EnumType.STRING)
    private FeeType feeType;

    private Double courseFee;

}
