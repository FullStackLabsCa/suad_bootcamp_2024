package io.reactivestax.active_life_canada.service;


import io.reactivestax.active_life_canada.domain.FamilyMember;
import io.reactivestax.active_life_canada.dto.FamilyGroupDto;
import io.reactivestax.active_life_canada.dto.FamilyMemberDto;
import io.reactivestax.active_life_canada.mapper.FamilyMemberMapper;
import io.reactivestax.active_life_canada.repository.FamilyMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                .build());
        familyMemberDto.setFamilyGroupId(familyGroupDto.getFamilyGroupId());
        FamilyMember familyMember = familyMemberMapper.toEntity(familyMemberDto);
        return familyMemberMapper.toDto(familyMemberRepository.save(familyMember));
    }

}
