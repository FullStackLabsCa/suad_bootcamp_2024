package io.reactivestax.activelifecanada.service;


import io.reactivestax.activelifecanada.domain.*;
import io.reactivestax.activelifecanada.dto.*;
import io.reactivestax.activelifecanada.exception.ResourceNotFoundException;
import io.reactivestax.activelifecanada.exception.UnauthorizedException;
import io.reactivestax.activelifecanada.mapper.CourseRegistrationMapper;
import io.reactivestax.activelifecanada.repository.FamilyCourseRegistrationRepository;
import io.reactivestax.activelifecanada.repository.FamilyMemberRepository;
import io.reactivestax.activelifecanada.repository.OfferedCourseRepository;
import io.reactivestax.activelifecanada.repository.WaitlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;


@Service
public class CourseRegistrationService {

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
    private AuthenticationService authenticationService;

    @Autowired
    private OfferedCourseRepository offeredCourseRepository;

    @Autowired
    private CourseRegistrationMapper courseRegistrationMapper;

    @Autowired
    private FamilyGroupService familyGroupService;


    @Transactional
    public CourseRegistrationDto save(Long familyMemberId, CourseRegistrationDto registrationDto) {
        FamilyMember familyMember = familyMemberService.findFamilyMemberById(familyMemberId);

//       if(Boolean.FALSE.equals(familyMember.getIsActive())) {
//           throw new UnauthorizedException("User is not active and unauthorized for Course registration");
//       }

        OfferedCourse offeredCourse = offeredCourseService.findById(registrationDto.getOfferedCourseId());
        CourseRegistration entity = courseRegistrationMapper.toEntity(registrationDto);

        if (offeredCourse.getNumberOfSeats() == 0 && offeredCourse.getWaitLists().size() <=
                offeredCourse.getTotalNumberOfSeats()) {
            /*pending to check if the familyMember is already added in the waitList for same offeredCourse
            * */
            WaitList waitList = WaitList.builder().familyMember(familyMember).offeredCourse(offeredCourse)
                    .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
            offeredCourse.getWaitLists().add(waitList);
            String message = "You have been added to the waiting list for " + offeredCourse.getCourse().getName() + " course";
            authenticationService.sendNotification(familyMember, message);
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

    public List<CourseRegistrationDto> bulkRegistrationToOfferedCourse(Long familyMemberId, List<CourseRegistrationDto> registrationDtos) {
       return registrationDtos.stream().map(courseRegistrationDto -> save(familyMemberId, courseRegistrationDto)).toList();
    }


    public List<CourseRegistrationDto> findEnrolledCourses(Long familyMemberId) {
        FamilyMember familyMember = familyMemberService.findFamilyMemberById(familyMemberId);
        List<CourseRegistration> courseRegistrations = familyMember.getCourseRegistrations();
        return courseRegistrations.stream().map(courseRegistrationMapper::toDto).toList();
    }

    @Transactional
    public String withdrawRegisteredCourse(Long registrationId) {
        CourseRegistration courseRegistration = courseRegistrationRepository.findById(registrationId).orElseThrow(() -> new ResourceNotFoundException("Course registration does not exist"));
        if (Boolean.TRUE.equals(courseRegistration.getIsWithdraw())) {
            throw new ResourceNotFoundException("This course is already dropped");
        }
        FamilyMember member = familyMemberService.findFamilyMemberById(courseRegistration.getFamilyMember().getFamilyMemberId());
        FamilyGroup familyGroup = familyGroupService.findById(member.getFamilyGroup().getFamilyGroupId());
        familyGroup.setCredits(courseRegistration.getCost());
        courseRegistration.setIsWithdraw(true);
        courseRegistration.setWithdrawCredits(courseRegistration.getWithdrawCredits() + (courseRegistration.getCost()));
        courseRegistrationRepository.save(courseRegistration);
        familyGroupService.saveGroup(familyGroup);
        OfferedCourse offeredCourse = offeredCourseService.findById(courseRegistration.getOfferedCourse().getOfferedCourseId());
        if(!offeredCourse.getWaitLists().isEmpty()){
            FamilyMember familyMember = offeredCourse.getWaitLists().get(0).getFamilyMember();
            String message = "Hurry! Seats is available to enroll";
            authenticationService.sendNotification(familyMember, message);
        }
        offeredCourseService.handleWithdraw(offeredCourse);
        return "Successfully withdrawn from " + offeredCourse.getCourse().getName() + " course";
    }

    private void setCourseRegistration(Long familyMemberId, CourseRegistration entity, FamilyMember familyMember, OfferedCourse offeredCourse) {
        entity.setIsWithdraw(false);
        entity.setCreatedTimeStamp(LocalDateTime.now());
        entity.setLastUpdatedTimeStamp(LocalDateTime.now());
        entity.setWithdrawCredits(0.0);
        entity.setLastUpdatedBy(familyMemberId);
        entity.setEnrollmentActorId(familyMemberId);
        entity.setEnrollmentActor(familyMember.getName());
        entity.setFamilyMember(familyMember);
        entity.setOfferedCourse(offeredCourse);
    }

//    public CourseRegistrationDto toDto(CourseRegistration courseRegistration){
//        CourseRegistrationDto dto = courseRegistrationMapper.toDto(courseRegistration);
////        dto.set
//
//    }
}
