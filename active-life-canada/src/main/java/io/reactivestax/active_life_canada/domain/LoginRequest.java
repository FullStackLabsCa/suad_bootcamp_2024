package io.reactivestax.active_life_canada.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
public class LoginRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loginRequestId;
    @Column(name = "created_ts")
    private LocalDateTime createdTimeStamp;

    @ManyToOne
    @JsonBackReference
    @ToString.Exclude
    @JoinColumn(name = "family_member_id")
    private FamilyMember familyMember;
}
