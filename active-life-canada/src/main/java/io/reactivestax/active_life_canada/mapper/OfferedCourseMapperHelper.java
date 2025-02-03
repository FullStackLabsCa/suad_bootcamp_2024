package io.reactivestax.active_life_canada.mapper;

import io.reactivestax.active_life_canada.domain.*;
import io.reactivestax.active_life_canada.dto.OfferedCourseFeeDto;
import io.reactivestax.active_life_canada.repository.CourseRepository;
import io.reactivestax.active_life_canada.repository.FamilyCourseRegistrationRepository;
import io.reactivestax.active_life_canada.repository.FamilyGroupRepository;
import io.reactivestax.active_life_canada.repository.LoginRequestRepository;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OfferedCourseMapperHelper {

    @Autowired
    private CourseRepository courseRepository;

//    @Autowired
//    private OfferedCourseFeeMapper offeredCourseFeeMapper;

    @Named("mapCourseIdToCourse")
    public Course mapToCourse(Long courseId) {
        return courseId != null ? courseRepository.findById(courseId).orElse(null) : null;
    }

//    @Named("mapCourseFeeDtoToFee")
//    public OfferedCourseFee mapToCourseFee(OfferedCourseFeeDto offeredCourseFeeDto) {
//       return offeredCourseFeeMapper.toEntity(offeredCourseFeeDto);
//    }

//    @Named("mapCourseFeeToDTo")
//    public OfferedCourseFeeDto mapToCourseFeeDto(OfferedCourseFee offeredCourseFee) {
//        return offeredCourseFeeMapper.toDto(offeredCourseFee);
//    }
}
