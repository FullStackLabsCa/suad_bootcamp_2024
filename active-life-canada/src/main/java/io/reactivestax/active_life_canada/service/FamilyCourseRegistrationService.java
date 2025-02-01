package io.reactivestax.active_life_canada.service;


import io.reactivestax.active_life_canada.domain.FamilyGroup;
import io.reactivestax.active_life_canada.domain.FamilyMember;
import io.reactivestax.active_life_canada.domain.SignUpRequest;
import io.reactivestax.active_life_canada.dto.FamilyGroupDto;
import io.reactivestax.active_life_canada.dto.FamilyMemberDto;
import io.reactivestax.active_life_canada.dto.LoginRequestDto;
import io.reactivestax.active_life_canada.dto.SignUpDto;
import io.reactivestax.active_life_canada.dto.ems.EmailDTO;
import io.reactivestax.active_life_canada.dto.ems.OtpDTO;
import io.reactivestax.active_life_canada.dto.ems.PhoneDTO;
import io.reactivestax.active_life_canada.dto.ems.SmsDTO;
import io.reactivestax.active_life_canada.enums.Status;
import io.reactivestax.active_life_canada.enums.StatusLevel;
import io.reactivestax.active_life_canada.exception.ResourceNotFoundException;
import io.reactivestax.active_life_canada.mapper.FamilyMemberMapper;
import io.reactivestax.active_life_canada.repository.FamilyMemberRepository;
import io.reactivestax.active_life_canada.repository.SignUpRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
public class FamilyMemberService {

    @Autowired
    private FamilyMemberRepository familyMemberRepository;

    @Autowired
    private FamilyMemberMapper familyMemberMapper;

    @Autowired
    private FamilyGroupService familyGroupService;

    @Autowired
    private SignUpRequestRepository signUpRequestRepository;

    @Autowired
    private EmsNotificationRestCallService emsNotificationRestCallService;

    @Autowired
    private EmsOtpRestCallService emsOtpRestCallService;

    @Transactional
    public FamilyMemberDto saveFamilyMemberAndCreateFamilyGroup(SignUpDto signUpDto) {
        FamilyGroup familyGroup = familyGroupService.saveGroupByFamilyGroupDto(FamilyGroupDto.builder().familyPin(signUpDto.getFamilyPin()).groupOwner(signUpDto.getName()).build());

        FamilyMember familyMember = familyMemberMapper.toFamilyMember(signUpDto);
        familyMember.setFamilyGroup(familyGroup);
        familyMember.setIsActive(false);

        familyGroup.setFamilyMember(List.of(familyMember));
        familyMemberRepository.save(familyMember);

        //generate uuid and store in signUp table
        UUID uuid = UUID.randomUUID();
        SignUpRequest signUpRequest = SignUpRequest.builder().familyMemberId(familyMember.getFamilyMemberId()).uuidToken(uuid).build();
        signUpRequestRepository.save(signUpRequest);
        //send it to the user via ems rest call in preferred method...
        sendNotification(familyMember, uuid);
        return familyMemberMapper.toDto(familyMember);
    }


    public FamilyMemberDto save(FamilyMemberDto familyMemberDto) {
        FamilyMember familyMember = familyMemberMapper.toEntity(familyMemberDto);
        return familyMemberMapper.toDto(familyMemberRepository.save(familyMember));
    }

    @Transactional
    public FamilyMemberDto addFamilyMembers(Long familyGroupId, FamilyMemberDto familyDto) {
        FamilyGroup familyGroup = familyGroupService.findById(familyGroupId);
        FamilyMember familyMember = familyMemberMapper.toEntity(familyDto);
        familyMember.setFamilyGroup(familyGroup);
        familyGroup.getFamilyMember().add(familyMember);
        familyMemberRepository.save(familyMember);
        return familyMemberMapper.toDto(familyMember);
    }

    public FamilyMemberDto findFamilyMemberDtoById(Long memberId) {
        FamilyMember familyMember = familyMemberRepository.findById(memberId).orElseThrow(() -> new ResourceNotFoundException("Family Member not found"));
        return familyMemberMapper.toDto(familyMember);
    }

    public FamilyMember findFamilyMemberById(Long memberId) {
        return familyMemberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Family Member not found"));
    }

    public FamilyMemberDto updateFamilyMember(Long familyGroupId, Long memberId, FamilyMemberDto familyMemberDto) {
        FamilyMember familyMember = familyMemberRepository.findById(memberId).orElseThrow(() -> new ResourceNotFoundException("Family Member not found"));
        familyMember.setName(familyMemberDto.getName());
        familyMember.setDob(familyMemberDto.getDob());
        familyMember.setEmailId(familyMemberDto.getEmailId());
        familyMember.setStreetNumber(familyMemberDto.getStreetNumber());
        familyMember.setStreetName(familyMemberDto.getStreetName());
        familyMember.setCity(familyMemberDto.getCity());
        familyMember.setProvince(familyMemberDto.getProvince());
        familyMember.setCountry(familyMemberDto.getCountry());
        familyMember.setHomePhone(familyMemberDto.getHomePhone());
        familyMember.setBusinessPhone(familyMemberDto.getBusinessPhone());
        familyMember.setLanguage(familyMemberDto.getLanguage());
        familyMember.setPreferredContact(familyMemberDto.getPreferredContact());
        familyMemberRepository.save(familyMember);

        return familyMemberMapper.toDto(familyMember);
    }

    public StatusLevel deleteFamilyMember(Long memberId) {
        FamilyMember familyMember = findFamilyMemberById(memberId);
        familyMember.setIsActive(false);
        familyMemberRepository.save(familyMember);
        return StatusLevel.SUCCESS;
    }

    @Transactional
    public StatusLevel activateMemberByUuid(Long familyMemberId, UUID uuid) {
        if (signUpRequestRepository.findByFamilyMemberIdAndUuidToken(familyMemberId, uuid) == null) {
            return StatusLevel.FAILED;
        }
        FamilyMember familyMember = findFamilyMemberById(familyMemberId);
        FamilyGroup familyGroup = familyGroupService.findById(familyMember.getFamilyGroup().getFamilyGroupId());
        familyGroup.setStatus("active");
        familyMember.setIsActive(true);
        familyGroupService.saveGroup(familyGroup);
        return StatusLevel.SUCCESS;
    }

    public StatusLevel loginFamilyMember(LoginRequestDto loginRequestDto) {
        FamilyMember familyMember = findFamilyMemberById(loginRequestDto.getFamilyMemberId());
        FamilyGroup familyGroup = familyGroupService.findById(familyMember.getFamilyGroup().getFamilyGroupId());

        if (!loginRequestDto.getFamilyPin().equalsIgnoreCase(familyGroup.getFamilyPin())) {
            throw new RuntimeException("Invalid FamilyPin...");
        }
      /*  call for the 2fa*/
      /*  send the otp on preferred contact*/
        OtpDTO generationOtpDto = OtpDTO.builder()
                .email(familyMember.getEmailId())
                .phone(familyMember.getHomePhone())
                .build();

        emsOtpRestCallService.sendOTP(generationOtpDto, familyMember.getPreferredContact());

        return StatusLevel.SUCCESS;
    }

    public Status login2FA(LoginRequestDto loginRequestDto) {
        FamilyMember familyMember = findFamilyMemberById(loginRequestDto.getFamilyMemberId());


        OtpDTO validateOtpDto = OtpDTO.builder()
                .email(familyMember.getEmailId())
                .phone(familyMember.getHomePhone())
                .validOtp(loginRequestDto.getOtp())
                .build();

        //verify otp
       return emsOtpRestCallService.verifyOTP(validateOtpDto);
    }

    private void sendNotification(FamilyMember familyMemberDto, UUID uuid) {
        if (familyMemberDto.getPreferredContact().equalsIgnoreCase("sms")) {
            SmsDTO smsDTO = new SmsDTO();
            smsDTO.setPhone(familyMemberDto.getHomePhone());
            smsDTO.setMessage(uuid.toString());
            emsNotificationRestCallService.sendSmsNotification(smsDTO);
        } else if (familyMemberDto.getPreferredContact().equalsIgnoreCase("email")) {
            EmailDTO emailDTO = new EmailDTO();
            emailDTO.setBody(uuid.toString());
            emailDTO.setReceiverEmailId(familyMemberDto.getEmailId());
            emailDTO.setSubject("Signup Activation");
            emsNotificationRestCallService.sendEmailSignUpNotification(emailDTO);
        } else {
            PhoneDTO phoneDTO = new PhoneDTO();
            phoneDTO.setOutgoingPhoneNumber(phoneDTO.getOutgoingPhoneNumber());
            emsNotificationRestCallService.sendPhoneNotification(phoneDTO);
        }
    }
}
