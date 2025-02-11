package io.reactivestax.active_life_canada.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long cartId;

    @ElementCollection
    @CollectionTable(name = "cart_offered_courses", joinColumns = @JoinColumn(name = "cart_id"))
    @Column(name = "offered_course_id")
    private List<Long> offeredCourseIds = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "family_member_id")
    @JsonManagedReference
    private FamilyMember familyMember;

    @Column(name = "created_ts")
    private LocalDateTime createdTimeStamp;

    private Boolean isActive;
}


