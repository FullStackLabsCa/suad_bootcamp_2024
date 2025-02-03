package io.reactivestax.active_life_canada.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WaitList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long waitListId;

    @ManyToOne
    @JsonBackReference
    @ToString.Exclude
    @JoinColumn(name = "offered_course_id")
    private OfferedCourse offeredCourse;

    @ManyToOne
    @JsonBackReference
    @ToString.Exclude
    @JoinColumn(name = "family_member_id")
    private FamilyMember familyMember;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;
}
