package io.reactivestax.active_life_canada.mapper;

import io.reactivestax.active_life_canada.domain.FamilyMember;
import io.reactivestax.active_life_canada.domain.OfferedCourse;
import io.reactivestax.active_life_canada.dto.FamilyMemberDto;
import io.reactivestax.active_life_canada.dto.OfferedCourseDto;
import io.reactivestax.active_life_canada.dto.SignUpDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OfferedCourseMapper {

    OfferedCourse toEntity(OfferedCourseDto offeredCourseDto);

    OfferedCourseDto toDto(OfferedCourse offeredCourse);
}
