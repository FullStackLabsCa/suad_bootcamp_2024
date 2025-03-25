package io.reactivestax.activelifecanada.mapper;

import io.reactivestax.activelifecanada.domain.FamilyMember;
import io.reactivestax.activelifecanada.repository.FamilyMemberRepository;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class FamilyGroupMapperHelper {
    
    @Autowired
    private FamilyMemberRepository familyMemberRepository;
    
    @Named("mapFamilyMemberIds")
    public List<Long> mapFamilyMemberIds(List<FamilyMember> familyMembers) {
        return familyMembers != null ? familyMembers.stream()
                .map(FamilyMember::getFamilyMemberId)
                .toList() : null;
    }
    
    @Named("mapFamilyMembers")
    public List<FamilyMember> mapFamilyMembers(List<Long> familyMemberIds) {
        return familyMemberIds != null ? familyMemberRepository.findAllById(familyMemberIds) : null;
    }
}
