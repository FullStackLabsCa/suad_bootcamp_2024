package io.reactivestax.active_life_canada.service;


import io.reactivestax.active_life_canada.domain.OfferedCourse;
import io.reactivestax.active_life_canada.domain.OfferedCourseFee;
import io.reactivestax.active_life_canada.dto.*;
import io.reactivestax.active_life_canada.enums.StatusLevel;
import io.reactivestax.active_life_canada.exception.ResourceNotFoundException;
import io.reactivestax.active_life_canada.mapper.OfferedCourseFeeMapper;
import io.reactivestax.active_life_canada.mapper.OfferedCourseMapper;
import io.reactivestax.active_life_canada.repository.OfferedCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class OfferedCourseService {

    @Autowired
    private OfferedCourseRepository offeredCourseRepository;

    @Autowired
    private OfferedCourseMapper offeredCourseMapper;

    @Autowired
    private OfferedCourseFeeMapper offeredCourseFeeMapper;


    @Transactional
    public OfferedCourseDto save(OfferedCourseDto offeredCourseDto) {
        OfferedCourse entity = toOfferedCourseEntity(offeredCourseDto);
        offeredCourseRepository.save(entity);
        return toOfferedCourseDto(entity);
    }

    public OfferedCourse toOfferedCourseEntity(OfferedCourseDto offeredCourseDto) {
        OfferedCourse entity = offeredCourseMapper.toEntity(offeredCourseDto);
        List<OfferedCourseFee> offeredCourseFees = offeredCourseDto.getOfferedCourseFeeDto().stream().map(offeredCourseFeeMapper::toEntity).toList();
        entity.setOfferedCourseFees(offeredCourseFees);
        entity.setCreatedTimeStamp(LocalDateTime.now());
        entity.setLastUpdatedTimeStamp(LocalDateTime.now());
        return entity;
    }

    public OfferedCourseDto toOfferedCourseDto(OfferedCourse offeredCourse) {
        OfferedCourseDto offeredCourseDto = offeredCourseMapper.toDto(offeredCourse);
        List<OfferedCourseFeeDto> offeredCourseFeeDtos = offeredCourse.getOfferedCourseFees().stream().map(offeredCourseFeeMapper::toDto).toList();
        offeredCourseDto.setOfferedCourseFeeDto(offeredCourseFeeDtos);
        return offeredCourseDto;
    }
    @Transactional
    public OfferedCourseDto updateOfferedCourse(Long offeredCourseId, OfferedCourseDto offeredCourseDto) {
        OfferedCourse offeredCourse = findById(offeredCourseId);
        if (offeredCourseDto.getStartDate() != null) offeredCourse.setStartDate(offeredCourseDto.getStartDate());
        if (offeredCourseDto.getEndDate() != null) offeredCourse.setStartDate(offeredCourseDto.getEndDate());
        if (offeredCourseDto.getNumberOfClassesOffered() != null)
            offeredCourse.setNumberOfClassesOffered(offeredCourseDto.getNumberOfClassesOffered());
        if (offeredCourseDto.getNumberOfSeats() != null)
            offeredCourse.setNumberOfSeats(offeredCourseDto.getNumberOfSeats());
        if (offeredCourseDto.getStartTime() != null) offeredCourse.setStartTime(offeredCourseDto.getStartTime());
        if (offeredCourseDto.getEndTime() != null) offeredCourse.setEndTime(offeredCourseDto.getEndTime());
        if (offeredCourseDto.getIsAllDayCourse() != null)
            offeredCourse.setIsAllDayCourse(offeredCourseDto.getIsAllDayCourse());
        if (offeredCourseDto.getRegistrationStartDate() != null)
            offeredCourse.setRegistrationStartDate(offeredCourseDto.getRegistrationStartDate());
        if (!offeredCourseDto.getOfferedCourseFeeDto().isEmpty()) {
            updateOfferedCourseFee(offeredCourseDto, offeredCourse);
        }
        offeredCourse.setLastUpdatedTimeStamp(LocalDateTime.now());
        offeredCourseRepository.save(offeredCourse);
        return toOfferedCourseDto(offeredCourse);
    }

    private void updateOfferedCourseFee(OfferedCourseDto offeredCourseDto, OfferedCourse offeredCourse) {
        offeredCourse.getOfferedCourseFees().clear();
        List<OfferedCourseFee> offeredCourseFees = offeredCourseDto.getOfferedCourseFeeDto().stream().map(offeredCourseFeeMapper::toEntity).toList();
        offeredCourse.getOfferedCourseFees().clear();
        for (OfferedCourseFee fee : offeredCourseFees) {
            fee.setOfferedCourse(offeredCourse);
        }
        offeredCourse.getOfferedCourseFees().addAll(offeredCourseFees);
    }

    public List<OfferedCourseDto> getAllOfferedCourse() {
        List<OfferedCourse> offeredCourses = offeredCourseRepository.findAll();
        return offeredCourses.stream().map(this::toOfferedCourseDto).toList();
    }

    public OfferedCourse findById(Long offeredCourseId){
        return offeredCourseRepository.findById(offeredCourseId).orElseThrow(() -> new ResourceNotFoundException("Offered course not found"));
    }


    public OfferedCourseDto getOfferedCourse(Long offeredCourseId) {
        OfferedCourse offeredCourse = findById(offeredCourseId);
        return toOfferedCourseDto(offeredCourse);
    }

    public StatusLevel deleteOfferedCourse(Long offeredCourseId) {
        OfferedCourse offeredCourse = findById(offeredCourseId);
        offeredCourse.setAvailableForEnrollment(false);
        offeredCourseRepository.save(offeredCourse);
        return StatusLevel.SUCCESS;
    }

    public void handleWithdraw(OfferedCourse offeredCourse) {
        offeredCourse.setNumberOfSeats(offeredCourse.getNumberOfSeats() + 1);
        offeredCourseRepository.save(offeredCourse);
    }

    public void updateNumberOfSeatsForRegistration(OfferedCourse offeredCourse) {
        offeredCourse.setNumberOfSeats(offeredCourse.getNumberOfSeats() - 1);
        offeredCourseRepository.save(offeredCourse);
    }

    public List<OfferedCourseDto> searchOfferedCourses(Long courseId, Long categoryId, Long facilityId, Long familyMemberId, Long subCategoryId) {
        Specification<OfferedCourse> spec = OfferedCourseSpecification.searchOfferedCourses(courseId, categoryId, facilityId, familyMemberId, subCategoryId);
        return  offeredCourseRepository.findAll(spec).stream().map(offeredCourseMapper::toDto).toList();
    }
}
