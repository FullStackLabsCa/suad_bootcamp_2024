package io.reactivestax.activelifecanada.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
