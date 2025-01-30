package io.reactivestax.active_life_canada.mapper;

import io.reactivestax.active_life_canada.domain.FamilyMember;
import io.reactivestax.active_life_canada.dto.FamilyMemberDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {FamilyMemberMapperHelper.class})
public interface FamilyMemberMapper {

    @Mapping(source = "familyGroup.familyGroupId", target = "familyGroupId")
    @Mapping(source = "familyCourseRegistration", target = "familyCourseRegistrationId", qualifiedByName = "mapFamilyCourseRegistration")
    @Mapping(source = "loginRequests", target = "loginRequestIds", qualifiedByName = "mapLoginRequestIds")
    FamilyMemberDto toDto(FamilyMember familyMember);

    @Mapping(source = "familyGroupId", target = "familyGroup", qualifiedByName = "mapFamilyGroup")
    @Mapping(source = "familyCourseRegistrationId", target = "familyCourseRegistration", qualifiedByName = "mapFamilyCourseRegistrationList")
    @Mapping(source = "loginRequestIds", target = "loginRequests", qualifiedByName = "mapLoginRequestList")
    FamilyMember toEntity(FamilyMemberDto familyMemberDto);
}
