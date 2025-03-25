package io.reactivestax.activelifecanada.domain;

import java.io.Serializable;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacilityFunctionId implements Serializable {
    private Long facilityId;
    private Long functionId;
}
