package io.reactivestax.active_life_canada.service;

import io.reactivestax.active_life_canada.domain.*;
import io.reactivestax.active_life_canada.dto.CourseRegistrationDto;
import io.reactivestax.active_life_canada.exception.ResourceNotFoundException;
import io.reactivestax.active_life_canada.mapper.CourseRegistrationMapper;
import io.reactivestax.active_life_canada.repository.CourseRegistrationRepository;
import io.reactivestax.active_life_canada.repository.OfferedCourseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CourseRegistrationServiceTest {

    @Autowired
    private CourseRegistrationService courseRegistrationService;

    @MockitoBean
    private FamilyMemberService familyMemberService;

    @MockitoBean
    private CourseRegistrationRepository courseRegistrationRepository;

    @MockitoBean
    private OfferedCourseService offeredCourseService;

    @MockitoBean
    private AuthenticationService authenticationService;

    @MockitoBean
    private OfferedCourseRepository offeredCourseRepository;

    @MockitoBean
    private CourseRegistrationMapper courseRegistrationMapper;

    @Test
    void testSave_WhenOfferedCourseHasNoSeats_AddsToWaitlist() {
        Long familyMemberId = 1L;
        CourseRegistrationDto registrationDto = new CourseRegistrationDto();
        registrationDto.setOfferedCourseId(2L);

        FamilyMember familyMember = new FamilyMember();
        familyMember.setFamilyMemberId(familyMemberId);
        familyMember.setIsActive(true);

        Course course = new Course();
        course.setName("Java Programming");

        OfferedCourse offeredCourse = new OfferedCourse();
        offeredCourse.setNumberOfSeats(0);
        offeredCourse.setWaitLists(new ArrayList<>());
        offeredCourse.setTotalNumberOfSeats(10);
        offeredCourse.setCourse(course);

        CourseRegistration entity = new CourseRegistration();

        when(familyMemberService.findFamilyMemberById(anyLong())).thenReturn(familyMember);
        when(offeredCourseService.findById(anyLong())).thenReturn(offeredCourse);
        when(courseRegistrationMapper.toEntity(any())).thenReturn(entity);

        CourseRegistrationDto result = courseRegistrationService.save(familyMemberId, registrationDto);

        assertEquals(registrationDto, result); // Ensure the method returns the correct DTO

        assertEquals(1, offeredCourse.getWaitLists().size());

        verify(authenticationService).sendNotification(eq(familyMember), contains("You have been added to the waiting list"));

        verify(offeredCourseRepository).save(offeredCourse);
    }

    @Test
    void testWithdrawRegisteredCourse_ThrowsResourceNotFound_WhenCourseRegistrationNotFound() {
        Long registrationId = 1L;
        when(courseRegistrationRepository.findById(registrationId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                courseRegistrationService.withdrawRegisteredCourse(registrationId)
        );
    }

    @Test
    void testWithdrawRegisteredCourse_ThrowsResourceNotFound_WhenCourseAlreadyWithdrawn() {
        Long registrationId = 2L;
        CourseRegistration courseRegistration = new CourseRegistration();
        courseRegistration.setIsWithdraw(true);

        when(courseRegistrationRepository.findById(registrationId)).thenReturn(Optional.of(courseRegistration));

        assertThrows(ResourceNotFoundException.class, () ->
                courseRegistrationService.withdrawRegisteredCourse(registrationId)
        );

    }

}