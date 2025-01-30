package io.reactivestax.active_life_canada.mapper;

import io.reactivestax.active_life_canada.domain.FamilyMember;
import io.reactivestax.active_life_canada.repository.FamilyMemberRepository;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
class FamilyGroupMapperHelper {
    
    @Autowired
    private FamilyMemberRepository familyMemberRepository;
    
    @Named("mapFamilyMemberIds")
    public List<Long> mapFamilyMemberIds(List<FamilyMember> familyMembers) {
        return familyMembers != null ? familyMembers.stream()
                .map(FamilyMember::getFamilyMemberId)
                .collect(Collectors.toList()) : null;
    }
    
    @Named("mapFamilyMembers")
    public List<FamilyMember> mapFamilyMembers(List<Long> familyMemberIds) {
        return familyMemberIds != null ? familyMemberRepository.findAllById(familyMemberIds) : null;
    }
}
