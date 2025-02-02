package io.reactivestax.active_life_canada.service;


import io.reactivestax.active_life_canada.domain.*;
import io.reactivestax.active_life_canada.dto.*;
import io.reactivestax.active_life_canada.exception.ResourceNotFoundException;
import io.reactivestax.active_life_canada.mapper.CourseRegistrationMapper;
import io.reactivestax.active_life_canada.mapper.FamilyMemberMapper;
import io.reactivestax.active_life_canada.repository.FamilyCourseRegistrationRepository;
import io.reactivestax.active_life_canada.repository.FamilyMemberRepository;
import io.reactivestax.active_life_canada.repository.OfferedCourseRepository;
import io.reactivestax.active_life_canada.repository.WaitlistRepository;
import io.reactivestax.active_life_canada.service.ems.EmsNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class FamilyCourseRegistrationService {

    @Autowired
    private FamilyCourseRegistrationRepository courseRegistrationRepository;

    @Autowired
    private FamilyMemberRepository familyMemberRepository;

    @Autowired
    private WaitlistRepository waitlistRepository;

    @Autowired
    private FamilyMemberService familyMemberService;

    @Autowired
    private OfferedCourseService offeredCourseService;

    @Autowired
    private OfferedCourseRepository offeredCourseRepository;

    @Autowired
    private CourseRegistrationMapper courseRegistrationMapper;


    @Transactional
    public CourseRegistrationDto save(Long familyMemberId, CourseRegistrationDto registrationDto) {
        OfferedCourse offeredCourse = offeredCourseService.findById(registrationDto.getOfferedCourseId());
        CourseRegistration entity = courseRegistrationMapper.toEntity(registrationDto);
        FamilyMember familyMember = familyMemberService.findFamilyMemberById(familyMemberId);
        if (offeredCourse.getNumberOfSeats() == 0 && offeredCourse.getWaitLists().size() <=
                offeredCourse.getTotalNumberOfSeats()) {
            WaitList waitList = WaitList.builder().familyMember(familyMember).offeredCourse(offeredCourse)
                    .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
            offeredCourse.getWaitLists().add(waitList);
            String message = "You have been added to the waiting list for " + offeredCourse.getCourse().getName() + " course";
            familyMemberService.sendNotification(familyMember, message);
            offeredCourseRepository.save(offeredCourse);
            return registrationDto;
        }
        setCourseRegistration(familyMemberId, entity, familyMember, offeredCourse);
        familyMember.getCourseRegistrations().add(entity);
        offeredCourse.getCourseRegistrations().add(entity);
        offeredCourse.setNumberOfSeats(offeredCourse.getNumberOfSeats() - 1);
        courseRegistrationRepository.save(entity);
        return courseRegistrationMapper.toDto(entity);
    }

    private void setCourseRegistration(Long familyMemberId, CourseRegistration entity, FamilyMember familyMember, OfferedCourse offeredCourse) {
        entity.setIsWithdraw(false);
        entity.setCreatedTimeStamp(LocalDateTime.now());
        entity.setLastUpdatedTimeStamp(LocalDateTime.now());
        entity.setWithdrawCredits(0.0);
        entity.setLastUpdatedBy(familyMemberId);
        entity.setEnrollmentActorId(familyMemberId);
        entity.setEnrollmentActor(familyMember.getName());
        entity.setEnrollmentDate(LocalDate.now());
        entity.setFamilyMember(familyMember);
        entity.setOfferedCourse(offeredCourse);
    }

    public List<CourseRegistrationDto> findEnrolledCourses(Long familyMemberId) {
        FamilyMember familyMember = familyMemberService.findFamilyMemberById(familyMemberId);
        List<CourseRegistration> courseRegistrations = familyMember.getCourseRegistrations();
        return courseRegistrations.stream().map(courseRegistrationMapper::toDto).toList();
    }

    @Transactional
    public String withdrawRegisteredCourse(Long id) {
        CourseRegistration courseRegistration = courseRegistrationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Course registration does not exist"));
        if (Boolean.TRUE.equals(courseRegistration.getIsWithdraw())) {
            throw new RuntimeException("This course is already dropped");
        }
        courseRegistration.setIsWithdraw(true);
        courseRegistration.setWithdrawCredits(courseRegistration.getWithdrawCredits() + (courseRegistration.getCost()));
        courseRegistrationRepository.save(courseRegistration);
        OfferedCourse offeredCourse = offeredCourseService.findById(courseRegistration.getOfferedCourse().getOfferedCourseId());
        offeredCourseService.handleWithdraw(offeredCourse);
        return "Successfully withdrawn from " + offeredCourse.getCourse().getName() + " course";
    }


}
