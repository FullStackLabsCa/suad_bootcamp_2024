package io.reactivestax.active_life_canada.service;


import io.reactivestax.active_life_canada.domain.FamilyMember;
import io.reactivestax.active_life_canada.dto.FamilyGroupDto;
import io.reactivestax.active_life_canada.dto.FamilyMemberDto;
import io.reactivestax.active_life_canada.enums.Status;
import io.reactivestax.active_life_canada.mapper.FamilyMemberMapper;
import io.reactivestax.active_life_canada.repository.FamilyMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FamilyMemberService {

    @Autowired
    private FamilyMemberRepository familyMemberRepository;

    @Autowired
    private FamilyMemberMapper familyMemberMapper;

    @Autowired
    private FamilyGroupService familyGroupService;

    public FamilyMemberDto saveFamilyMemberAndCreateFamilyGroup(FamilyMemberDto familyMemberDto) {
        FamilyGroupDto familyGroupDto = familyGroupService.saveGroup(FamilyGroupDto.builder()
                .familyPin(familyMemberDto.getFamilyPin())
                        .groupOwner(familyMemberDto.getName())
                .build());
        familyMemberDto.setFamilyGroupId(familyGroupDto.getFamilyGroupId());
        FamilyMember familyMember = familyMemberMapper.toEntity(familyMemberDto);
        return familyMemberMapper.toDto(familyMemberRepository.save(familyMember));
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

    public FamilyMemberDto updateFamilyMember(Long familyGroupId, Long memberId, FamilyMemberDto familyMemberDto){
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
        familyMember.setIsActive(familyMemberDto.getIsActive());
        familyMember.setPreferredContact(familyMemberDto.getPreferredContact());
        familyMember.setFamilyCourseRegistrationId(familyMemberDto.getFamilyCourseRegistrationId());
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
}
