package io.reactivestax.active_life_canada.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseRegistrationDto {

    @Null(groups = {Create.class}, message = "Id should not be set when registering a course")
    @NotNull(groups = {Update.class}, message = "Id required when updating registration")
    private Long familyCourseRegistrationId;

    private Double cost;
    private LocalDate enrollmentDate;
    private Boolean isWithdraw;
    private Double withdrawCredits;
    private String enrollmentActor;
//    private Long enrollmentActorId;
    @NotNull(message = "Offered course Id cannot be null")
    @Positive(message = "Offered course Id must be positive")
    private Long offeredCourseId;

    public interface Create{}

    public interface Update{}
}
