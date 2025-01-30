package io.reactivestax.active_life_canada.service;


import io.reactivestax.active_life_canada.domain.FamilyGroup;
import io.reactivestax.active_life_canada.domain.FamilyMember;
import io.reactivestax.active_life_canada.dto.FamilyGroupDto;
import io.reactivestax.active_life_canada.dto.FamilyMemberDto;
import io.reactivestax.active_life_canada.mapper.FamilyGroupMapper;
import io.reactivestax.active_life_canada.mapper.FamilyMemberMapper;
import io.reactivestax.active_life_canada.repository.FamilyGroupRepository;
import io.reactivestax.active_life_canada.repository.FamilyMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FamilyGroupService {

    @Autowired
    private FamilyGroupRepository familyGroupRepository;

    @Autowired
    private FamilyGroupMapper familyGroupMapper;


    public FamilyGroupDto saveGroup(FamilyGroupDto familyGroupDto){
        FamilyGroup entity = familyGroupMapper.toEntity(familyGroupDto);
        return familyGroupMapper.toDto(familyGroupRepository.save(entity));
    }

}
