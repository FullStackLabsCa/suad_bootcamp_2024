package io.reactivestax.active_life_canada.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.reactivestax.active_life_canada.repository.FamilyMemberRepository;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfferedCourseDto {

    private String barCode;

    @NotNull(message = "Start Date cannot be null")
    @FutureOrPresent(message = "Start Date can not be past date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;


    @NotNull(message = "End Date cannot be null")
    @FutureOrPresent(message = "End Date can not be past date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private Integer numberOfClassesOffered;


    @NotNull(message = "Start Time cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime startTime;

    @NotNull(message = "End Time cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime endTime;

    private Boolean isAllDayCourse;

    @Min(value = 1, message = "Number of seats must be greater than 0.")
    private Integer numberOfSeats;

    private Integer totalNumberOfSeats;

    @NotNull(message = "Registration Start Date cannot be null")
    @Future(message = "Registration date must be in the future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate registrationStartDate;

    @NotNull(message = "Course ID is required.")
    private Long courseId;


    private Boolean availableForEnrollment;

    private List<OfferedCourseFeeDto> offeredCourseFeeDto = new ArrayList<>();


}
