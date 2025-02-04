package io.reactivestax.active_life_canada.service;

import io.reactivestax.active_life_canada.domain.FamilyMember;
import io.reactivestax.active_life_canada.dto.CourseRegistrationDto;
import io.reactivestax.active_life_canada.dto.ems.EmailDTO;
import io.reactivestax.active_life_canada.dto.ems.PhoneDTO;
import io.reactivestax.active_life_canada.exception.UnauthorizedException;
import io.reactivestax.active_life_canada.service.ems.EmsNotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthenticationServiceTest {

    @Autowired
    private CourseRegistrationService courseRegistrationService;

    @MockitoBean
    private FamilyMemberService familyMemberService;

    @MockitoBean
    private OfferedCourseService offeredCourseService;

    @MockitoBean
    private EmsNotificationService emsNotificationService;

    @Autowired
    private AuthenticationService authenticationService;

    @Test
    void testSave_UnauthorizedException() {
        Long familyMemberId = 1L;
        CourseRegistrationDto registrationDto = new CourseRegistrationDto();

        FamilyMember familyMember = new FamilyMember();
        familyMember.setIsActive(false);

        when(familyMemberService.findFamilyMemberById(familyMemberId)).thenReturn(familyMember);

        assertThrows(UnauthorizedException.class, () ->
                courseRegistrationService.save(familyMemberId, registrationDto)
        );
    }

    @Test
    void testSendNotification_EmailContact() {
        // Setup
        FamilyMember familyMember = new FamilyMember();
        familyMember.setPreferredContact("email");
        familyMember.setEmailId("test@example.com");
        String message = "Test Message";

        // Mock behavior of sending email notification
        doNothing().when(emsNotificationService).sendEmailSignUpNotification(any(EmailDTO.class));

        // Call method
        authenticationService.sendNotification(familyMember, message);

        // Verify the email notification was sent
        verify(emsNotificationService).sendEmailSignUpNotification(any(EmailDTO.class));
    }

    @Test
     void testSendNotification_PhoneContact() {
        // Setup
        FamilyMember familyMember = new FamilyMember();
        familyMember.setPreferredContact("phone");  // Neither sms nor email
        String message = "Test Message";

        // Mock behavior of sending phone notification
        doNothing().when(emsNotificationService).sendPhoneNotification(any(PhoneDTO.class));

        // Call method
        authenticationService.sendNotification(familyMember, message);

        // Verify the phone notification was sent
        verify(emsNotificationService).sendPhoneNotification(any(PhoneDTO.class));
    }
}
