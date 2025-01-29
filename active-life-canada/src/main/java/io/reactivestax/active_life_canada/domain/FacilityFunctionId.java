package io.reactivestax.active_life_canada.domain;

import java.io.Serializable;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacilityFunctionId implements Serializable {
    private Long facilityId;
    private Long functionId;
}
