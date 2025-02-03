package io.reactivestax.active_life_canada.service;


import io.reactivestax.active_life_canada.domain.FamilyGroup;
import io.reactivestax.active_life_canada.domain.FamilyMember;
import io.reactivestax.active_life_canada.dto.FamilyMemberDto;
import io.reactivestax.active_life_canada.enums.StatusLevel;
import io.reactivestax.active_life_canada.exception.ResourceNotFoundException;
import io.reactivestax.active_life_canada.mapper.FamilyMemberMapper;
import io.reactivestax.active_life_canada.repository.FamilyMemberRepository;
import io.reactivestax.active_life_canada.repository.SignUpRequestRepository;
import io.reactivestax.active_life_canada.service.ems.EmsNotificationService;
import io.reactivestax.active_life_canada.service.ems.EmsOtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private EmsNotificationService emsNotificationService;

    @Autowired
    private EmsOtpService emsOtpService;

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

    public FamilyMemberDto updateFamilyMember(Long memberId, FamilyMemberDto familyMemberDto) {
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
}
