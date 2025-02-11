package io.reactivestax.active_life_canada.service;


import io.reactivestax.active_life_canada.domain.FamilyGroup;
import io.reactivestax.active_life_canada.domain.FamilyMember;
import io.reactivestax.active_life_canada.domain.LoginRequest;
import io.reactivestax.active_life_canada.domain.SignUpRequest;
import io.reactivestax.active_life_canada.dto.FamilyGroupDto;
import io.reactivestax.active_life_canada.dto.FamilyMemberDto;
import io.reactivestax.active_life_canada.dto.LoginRequestDto;
import io.reactivestax.active_life_canada.dto.SignUpDto;
import io.reactivestax.active_life_canada.dto.ems.EmailDTO;
import io.reactivestax.active_life_canada.dto.ems.OtpDTO;
import io.reactivestax.active_life_canada.dto.ems.PhoneDTO;
import io.reactivestax.active_life_canada.dto.ems.SmsDTO;
import io.reactivestax.active_life_canada.enums.StatusLevel;
import io.reactivestax.active_life_canada.exception.UnauthorizedException;
import io.reactivestax.active_life_canada.mapper.FamilyMemberMapper;
import io.reactivestax.active_life_canada.mapper.LoginRequestMapper;
import io.reactivestax.active_life_canada.repository.FamilyMemberRepository;
import io.reactivestax.active_life_canada.repository.LoginRequestRepository;
import io.reactivestax.active_life_canada.repository.SignUpRequestRepository;
import io.reactivestax.active_life_canada.service.ems.EmsNotificationService;
import io.reactivestax.active_life_canada.service.ems.EmsOtpService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


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


    @Transactional
    public FamilyMemberDto signUpAndCreateFamilyGroup(SignUpDto signUpDto) {
        FamilyGroup familyGroup = familyGroupService.saveGroupByFamilyGroupDto(FamilyGroupDto.builder().familyPin(signUpDto.getFamilyPin()).groupOwner(signUpDto.getName()).build());

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
        FamilyGroup familyGroup = familyGroupService.findById(familyMember.getFamilyGroup().getFamilyGroupId());

        if (!loginRequestDto.getFamilyPin().equalsIgnoreCase(familyGroup.getFamilyPin())) {
            throw new UnauthorizedException("Invalid FamilyPin...");
        }
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

    public String login2FA(LoginRequestDto loginRequestDto, HttpSession session) {
        FamilyMember familyMember = familyMemberService.findFamilyMemberById(loginRequestDto.getFamilyMemberId());
        OtpDTO validateOtpDto = OtpDTO.builder()
                .email(familyMember.getEmailId())
                .phone(familyMember.getHomePhone())
                .validOtp(loginRequestDto.getOtp())
                .build();
//        session.setAttribute("username", familyMember.getName());
        return emsOtpService.verifyOTP(validateOtpDto).toString() + " and the session is active for: " + familyMember.getName();
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
}
