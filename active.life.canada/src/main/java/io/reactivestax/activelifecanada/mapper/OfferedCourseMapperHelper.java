package io.reactivestax.activelifecanada.mapper;

import io.reactivestax.activelifecanada.domain.*;
import io.reactivestax.activelifecanada.repository.CourseRepository;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
