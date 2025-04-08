package io.reactivestax.activelifecanada.mapper;

import io.reactivestax.activelifecanada.domain.FamilyGroup;
import io.reactivestax.activelifecanada.dto.FamilyGroupDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {FamilyMemberMapperHelper.class, FamilyGroupMapperHelper.class})
public interface FamilyGroupMapper {

//    @Mapping(source = "familyMemberIds", target = "familyMember", qualifiedByName = "mapFamilyMembers")
    FamilyGroup toEntity(FamilyGroupDto familyGroupDto);
    
//    @Mapping(source = "familyMember", target = "familyMemberIds", qualifiedByName = "mapFamilyMemberIds")
    FamilyGroupDto toDto(FamilyGroup familyGroup);
}
