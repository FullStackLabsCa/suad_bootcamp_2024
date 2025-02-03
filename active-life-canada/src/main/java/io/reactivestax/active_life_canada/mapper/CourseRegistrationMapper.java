package io.reactivestax.active_life_canada.mapper;

import io.reactivestax.active_life_canada.domain.CourseRegistration;
import io.reactivestax.active_life_canada.dto.CourseRegistrationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseRegistrationMapper {

    CourseRegistration toEntity(CourseRegistrationDto courseRegistrationDto);

    @Mapping(source = "offeredCourse.offeredCourseId", target = "offeredCourseId")
    CourseRegistrationDto toDto(CourseRegistration courseRegistration);
}