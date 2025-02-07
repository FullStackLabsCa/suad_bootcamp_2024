package io.reactivestax.active_life_canada.mapper;

import io.reactivestax.active_life_canada.domain.OfferedCourse;
import io.reactivestax.active_life_canada.dto.OfferedCourseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OfferedCourseMapperHelper.class})
public interface OfferedCourseMapper {

    @Mapping(source = "courseId", target = "course", qualifiedByName = "mapCourseIdToCourse")
    OfferedCourse toEntity(OfferedCourseDto offeredCourseDto);

    @Mapping(source = "course.courseId", target = "courseId")
    OfferedCourseDto toDto(OfferedCourse offeredCourse);
}
