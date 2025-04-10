package io.reactivestax.activelifecanada.service;


import io.reactivestax.activelifecanada.domain.*;
import io.reactivestax.activelifecanada.dto.FamilyGroupDto;
import io.reactivestax.activelifecanada.dto.FamilyMemberDto;
import io.reactivestax.activelifecanada.dto.LoginRequestDto;
import io.reactivestax.activelifecanada.dto.SignUpDto;
import io.reactivestax.activelifecanada.dto.ems.EmailDTO;
import io.reactivestax.activelifecanada.dto.ems.OtpDTO;
import io.reactivestax.activelifecanada.dto.ems.PhoneDTO;
import io.reactivestax.activelifecanada.dto.ems.SmsDTO;
import io.reactivestax.activelifecanada.enums.Status;
import io.reactivestax.activelifecanada.enums.StatusLevel;
import io.reactivestax.activelifecanada.exception.UnauthorizedException;
import io.reactivestax.activelifecanada.mapper.FamilyGroupMapper;
import io.reactivestax.activelifecanada.mapper.FamilyMemberMapper;
import io.reactivestax.activelifecanada.mapper.LoginRequestMapper;
import io.reactivestax.activelifecanada.repository.FamilyMemberRepository;
import io.reactivestax.activelifecanada.repository.LoginRequestRepository;
import io.reactivestax.activelifecanada.repository.SignUpRequestRepository;
import io.reactivestax.activelifecanada.service.ems.EmsNotificationService;
import io.reactivestax.activelifecanada.service.ems.EmsOtpService;
import io.reactivestax.activelifecanada.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class AuthenticationService {

    @Autowired
    private FamilyMemberMapper familyMemberMapper;

    @Autowired
    private FamilyGroupService familyGroupService;

    @Autowired
    private FamilyMemberRepository familyMemberRepository;

    @Autowired
    private FamilyMemberService familyMemberService;

    @Autowired
    private SignUpRequestRepository signUpRequestRepository;

    @Autowired
    private EmsNotificationService emsNotificationService;

    @Autowired
    private EmsOtpService emsOtpService;

    @Autowired
    private LoginRequestRepository loginRequestRepository;

    @Autowired
    private LoginRequestMapper loginRequestMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FamilyGroupMapper familyGroupMapper;


    @Transactional
    public FamilyMemberDto signUpAndCreateFamilyGroup(SignUpDto signUpDto) {
        FamilyGroup familyGroup = familyGroupService.saveGroup(FamilyGroup.builder()
                .familyPin(passwordEncoder.encode(signUpDto.getFamilyPin()))
                .groupOwner(signUpDto.getName())
                .isGroupOwner(true)
                .build()
        );

        FamilyMember familyMember = familyMemberMapper.toFamilyMember(signUpDto);
        familyMember.setFamilyGroup(familyGroup);
        familyMember.setIsActive(false);
        familyGroup.setFamilyMember(List.of(familyMember));
        familyMemberRepository.save(familyMember);

        UUID uuid = UUID.randomUUID();
        SignUpRequest signUpRequest = SignUpRequest.builder().familyMemberId(familyMember.getFamilyMemberId()).uuidToken(uuid).build();
        signUpRequestRepository.save(signUpRequest);
        sendNotification(familyMember, uuid.toString());
        return familyMemberMapper.toDto(familyMember);
    }


    public StatusLevel loginFamilyMember(LoginRequestDto loginRequestDto) {
        FamilyMember familyMember = familyMemberService.findFamilyMemberById(loginRequestDto.getFamilyMemberId());
        FamilyGroup familyGroup = familyGroupService.findByGroupId(familyMember.getFamilyGroup().getFamilyGroupId());

        if (!passwordEncoder.matches(loginRequestDto.getFamilyPin(), familyGroup.getFamilyPin())) {
            throw new UnauthorizedException("Invalid FamilyPin...");
        }

//        if (!loginRequestDto.getFamilyPin().equalsIgnoreCase(familyGroup.getFamilyPin())) {
//            throw new UnauthorizedException("Invalid FamilyPin...");
//        }
        /*  call for the 2fa*/
        /*  send the otp on preferred contact*/
        OtpDTO generationOtpDto = OtpDTO.builder()
                .email(familyMember.getEmailId())
                .phone(familyMember.getHomePhone())
                .build();

        LoginRequest entity = loginRequestMapper.toEntity(loginRequestDto);
        entity.setFamilyMember(familyMember);
        loginRequestRepository.save(entity);

        emsOtpService.sendOTP(generationOtpDto, familyMember.getPreferredContact());
        return StatusLevel.SUCCESS;

    }

    public Map<String, String> login2FA(LoginRequestDto loginRequestDto) {
        FamilyMember familyMember = familyMemberService.findFamilyMemberById(loginRequestDto.getFamilyMemberId());
        OtpDTO validateOtpDto = OtpDTO.builder()
                .email(familyMember.getEmailId())
                .phone(familyMember.getHomePhone())
                .validOtp(loginRequestDto.getOtp())
                .build();

        Status status = emsOtpService.verifyOTP(validateOtpDto);
        if (status.equals(Status.VALID)) {
            familyMember.setIsActive(true);
            Map<String, String> userData = new HashMap<>();
            userData.put("userName", familyMember.getName());
            userData.put("role", "ROLE_ADMIN");
            userData.put("userId", familyMember.getFamilyMemberId().toString());
            String token = jwtUtil.generateToken(userData);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return response;
        }
        throw new UsernameNotFoundException("Invalid User credentials..");
    }

    public void sendNotification(FamilyMember familyMember, String message) {
        if (familyMember.getPreferredContact().equalsIgnoreCase("sms")) {
            SmsDTO smsDTO = new SmsDTO();
            smsDTO.setPhone(familyMember.getHomePhone());
            smsDTO.setMessage(message);
            emsNotificationService.sendSmsNotification(smsDTO);
        } else if (familyMember.getPreferredContact().equalsIgnoreCase("email")) {
            EmailDTO emailDTO = new EmailDTO();
            emailDTO.setBody(message);
            emailDTO.setReceiverEmailId(familyMember.getEmailId());
            emailDTO.setSubject("Signup Activation");
            emsNotificationService.sendEmailSignUpNotification(emailDTO);
        } else {
            PhoneDTO phoneDTO = new PhoneDTO();
            phoneDTO.setOutgoingPhoneNumber(phoneDTO.getOutgoingPhoneNumber());
            emsNotificationService.sendPhoneNotification(phoneDTO);
        }
    }

    public FamilyGroupDto getFamilyGroupDetails(Long familyMemberId) {
        FamilyGroup familyGroup = familyGroupService.findByMemberId(familyMemberId);
        FamilyMember familyMember = familyMemberService.findFamilyMemberById(familyMemberId);
        if (!familyGroup.getGroupOwner().equalsIgnoreCase(familyMember.getName())) {
            familyGroup.setFamilyMember(List.of(familyMember));
        }

      long totalCourseRegistered = familyGroup.getFamilyMember().stream()
              .flatMap(member -> member.getCourseRegistrations().stream())
              .filter(courseRegistration -> !courseRegistration.getIsWithdraw())
                .count();

        long withDrawCount = familyGroup.getFamilyMember().stream()
                .flatMap(member -> member.getCourseRegistrations().stream())
                .filter(CourseRegistration::getIsWithdraw)
                .count();

        Double totalCost = familyGroup.getFamilyMember().stream()
                .flatMap(member -> member.getCourseRegistrations().stream())
                .filter(courseRegistration -> !courseRegistration.getIsWithdraw())
                .map(CourseRegistration::getCost)
                .reduce(0.0, Double::sum);


        FamilyGroupDto dto = familyGroupMapper.toDto(familyGroup);
        dto.setTotalCostOfEnrolledCourses(totalCost);
        dto.setTotalCourseEnrolled(totalCourseRegistered);
        dto.setTotalWithdrawCourses(withDrawCount);
        return dto;
    }
}
