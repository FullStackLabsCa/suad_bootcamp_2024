package io.reactivestax.active_life_canada.mapper;

import io.reactivestax.active_life_canada.domain.FamilyMember;
import io.reactivestax.active_life_canada.domain.OfferedCourse;
import io.reactivestax.active_life_canada.dto.FamilyMemberDto;
import io.reactivestax.active_life_canada.dto.OfferedCourseDto;
import io.reactivestax.active_life_canada.dto.OfferedCourseFeeDto;
import io.reactivestax.active_life_canada.dto.SignUpDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OfferedCourseMapperHelper.class})
public interface OfferedCourseMapper {

    @Mapping(source = "courseId", target = "course", qualifiedByName = "mapCourseIdToCourse")
//    @Mapping(source = "offeredCourseFeeDto", target = "offeredCourseFee", qualifiedByName = "mapCourseFeeDtoToFee")
//    @Mapping(source = "offeredCourseFeeDto", target = "offeredCourseFee")
    OfferedCourse toEntity(OfferedCourseDto offeredCourseDto);

    @Mapping(source = "course.courseId", target = "courseId")
//    @Mapping(source = "offeredCourseFee", target = "offeredCourseFeeDto", qualifiedByName = "mapCourseFeeToDTo")
//    @Mapping(source = "offeredCourseFee", target = "offeredCourseFeeDto")
    OfferedCourseDto toDto(OfferedCourse offeredCourse);
}
