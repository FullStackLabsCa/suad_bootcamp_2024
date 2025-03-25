package io.reactivestax.activelifecanada.mapper;

import io.reactivestax.activelifecanada.domain.OfferedCourse;
import io.reactivestax.activelifecanada.dto.OfferedCourseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OfferedCourseMapperHelper.class})
public interface OfferedCourseMapper {

    @Mapping(source = "courseId", target = "course", qualifiedByName = "mapCourseIdToCourse")
    OfferedCourse toEntity(OfferedCourseDto offeredCourseDto);

    @Mapping(source = "course.courseId", target = "courseId")
    OfferedCourseDto toDto(OfferedCourse offeredCourse);
}
