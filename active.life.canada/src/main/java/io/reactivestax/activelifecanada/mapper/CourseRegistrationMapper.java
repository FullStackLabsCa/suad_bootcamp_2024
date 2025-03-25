package io.reactivestax.activelifecanada.mapper;

import io.reactivestax.activelifecanada.domain.CourseRegistration;
import io.reactivestax.activelifecanada.dto.CourseRegistrationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseRegistrationMapper {

    CourseRegistration toEntity(CourseRegistrationDto courseRegistrationDto);

    @Mapping(source = "offeredCourse.offeredCourseId", target = "offeredCourseId")
    CourseRegistrationDto toDto(CourseRegistration courseRegistration);
}