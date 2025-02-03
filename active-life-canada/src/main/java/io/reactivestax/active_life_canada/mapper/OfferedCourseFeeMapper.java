package io.reactivestax.active_life_canada.mapper;

import io.reactivestax.active_life_canada.domain.OfferedCourse;
import io.reactivestax.active_life_canada.domain.OfferedCourseFee;
import io.reactivestax.active_life_canada.dto.OfferedCourseDto;
import io.reactivestax.active_life_canada.dto.OfferedCourseFeeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface OfferedCourseFeeMapper {

    OfferedCourseFee toEntity(OfferedCourseFeeDto offeredCourseFeeDto);

    OfferedCourseFeeDto toDto(OfferedCourseFee offeredCourseFee);
}
