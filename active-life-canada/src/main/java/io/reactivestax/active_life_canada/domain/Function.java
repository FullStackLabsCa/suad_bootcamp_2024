package io.reactivestax.active_life_canada.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Function {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long functionId;
    private String description;

    @OneToMany(mappedBy = "function", cascade = CascadeType.ALL)
    private List<FacilityFunction> facilityFunctions = new ArrayList<>();

    @Column(name = "created_ts")
    private LocalDateTime createdTimeStamp;
    @Column(name = "last_updated_ts")
    private LocalDateTime lastUpdatedTimeStamp;
    private  Long createdBy;
    private Long lastUpdatedBy;
}
