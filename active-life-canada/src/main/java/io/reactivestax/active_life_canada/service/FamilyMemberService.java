package io.reactivestax.active_life_canada.service;


import io.reactivestax.active_life_canada.domain.FamilyMember;
import io.reactivestax.active_life_canada.domain.SignUpRequest;
import io.reactivestax.active_life_canada.dto.FamilyGroupDto;
import io.reactivestax.active_life_canada.dto.FamilyMemberDto;
import io.reactivestax.active_life_canada.dto.ems.EmailDTO;
import io.reactivestax.active_life_canada.dto.ems.PhoneDTO;
import io.reactivestax.active_life_canada.dto.ems.SmsDTO;
import io.reactivestax.active_life_canada.enums.Status;
import io.reactivestax.active_life_canada.mapper.FamilyMemberMapper;
import io.reactivestax.active_life_canada.repository.FamilyMemberRepository;
import io.reactivestax.active_life_canada.repository.SignUpRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private EmsRestCallService emsRestCallService;

    public FamilyMemberDto saveFamilyMemberAndCreateFamilyGroup(FamilyMemberDto familyMemberDto) {
        FamilyGroupDto familyGroupDto = familyGroupService.saveGroup(FamilyGroupDto.builder().familyPin(familyMemberDto.getFamilyPin()).groupOwner(familyMemberDto.getName()).build());
        familyMemberDto.setFamilyGroupId(familyGroupDto.getFamilyGroupId());
        FamilyMember familyMember = familyMemberMapper.toEntity(familyMemberDto);
        FamilyMemberDto savedDto = familyMemberMapper.toDto(familyMemberRepository.save(familyMember));

        //generate uuid and store in signUp table
        UUID uuid = UUID.randomUUID();
        SignUpRequest signUpRequest = SignUpRequest.builder().familyMemberId(savedDto.getFamilyMemberId()).uuidToken(uuid).build();
        signUpRequestRepository.save(signUpRequest);
        //send it to the user via ems rest call in preferred method...
        sendNotification(familyMemberDto, uuid);
        return savedDto;
    }

    private void sendNotification(FamilyMemberDto familyMemberDto, UUID uuid) {
        if (familyMemberDto.getPreferredContact().equalsIgnoreCase("sms")) {
            SmsDTO smsDTO = new SmsDTO();
            smsDTO.setPhone(familyMemberDto.getHomePhone());
            smsDTO.setMessage(uuid.toString());
            emsRestCallService.sendSmsNotification(smsDTO);
        } else if (familyMemberDto.getPreferredContact().equalsIgnoreCase("email")) {
            EmailDTO emailDTO = new EmailDTO();
            emailDTO.setBody(uuid.toString());
            emailDTO.setReceiverEmailId(familyMemberDto.getEmailId());
            emailDTO.setSubject("Signup Activation");
            emsRestCallService.sendEmailSignUpNotification(emailDTO);
        } else {
            PhoneDTO phoneDTO = new PhoneDTO();
            phoneDTO.setOutgoingPhoneNumber(phoneDTO.getOutgoingPhoneNumber());
            emsRestCallService.sendPhoneNotification(phoneDTO);
        }
    }

    public FamilyMemberDto save(FamilyMemberDto familyMemberDto) {
        FamilyMember familyMember = familyMemberMapper.toEntity(familyMemberDto);
        return familyMemberMapper.toDto(familyMemberRepository.save(familyMember));
    }


    public FamilyMemberDto addFamilyMembers(Long familyGroupId, FamilyMemberDto familyDto) {
        FamilyMember entity = familyMemberMapper.toEntity(familyDto);
        entity.setFamilyGroup(familyGroupService.findById(familyGroupId));
        return familyMemberMapper.toDto(familyMemberRepository.save(entity));
    }

    public FamilyMemberDto findFamilyMemberById(Long memberId) {
        FamilyMember familyMember = familyMemberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Family Member not found"));
        return familyMemberMapper.toDto(familyMember);
    }

    public FamilyMemberDto updateFamilyMember(Long familyGroupId, Long memberId, FamilyMemberDto familyMemberDto) {
        FamilyMemberDto familyMember = findFamilyMemberById(memberId);
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
        familyMember.setMemberLoginId(familyMemberDto.getMemberLoginId());
        familyMember.setPreferredContact(familyMemberDto.getPreferredContact());
        familyMember.setFamilyCourseRegistrationIds(familyMemberDto.getFamilyCourseRegistrationIds());
        familyMember.setFamilyGroupId(familyGroupId);
        familyMember.setLoginRequestIds(familyMemberDto.getLoginRequestIds());
        return save(familyMember);
    }

    public Status deleteFamilyMember(Long memberId) {
        FamilyMemberDto familyMemberDto = findFamilyMemberById(memberId);
        familyMemberDto.setIsActive(false);
        save(familyMemberDto);
        return Status.SUCCESS;
    }

    public Status validateUUUIDToken(Long familyMemberId, UUID uuid) {
        if (signUpRequestRepository.findByFamilyMemberIdAndUuidToken(familyMemberId, uuid) == null) {
            return Status.FAILED;
        }
        FamilyMemberDto familyMemberDto = findFamilyMemberById(familyMemberId);
        familyMemberDto.setIsActive(true);
        save(familyMemberDto);
        return Status.SUCCESS;
    }
}
