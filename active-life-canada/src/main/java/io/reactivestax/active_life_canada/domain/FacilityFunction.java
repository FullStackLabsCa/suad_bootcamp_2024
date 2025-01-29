package io.reactivestax.active_life_canada.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacilityFunction {

    @EmbeddedId
    private FacilityFunctionId id;

    @ManyToOne
    @MapsId("facilityId")
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;


    @ManyToOne
    @MapsId("functionId")
    @JoinColumn(name = "function_id", nullable = false)
    private Function function;

    @Column(name = "created_ts")
    private LocalDateTime createdTimeStamp;

    @Column(name = "last_updated_ts")
    private LocalDateTime lastUpdateTimeStamp;

    private Long createdBy;

    private Long lastUpdatedBy;

}
