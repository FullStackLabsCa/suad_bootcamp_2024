package io.reactivestax.active_life_canada.mapper;

import io.reactivestax.active_life_canada.domain.FamilyMember;
import io.reactivestax.active_life_canada.dto.FamilyMemberDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {FamilyMemberMapperHelper.class})
public interface FamilyMemberMapper {

    @Mapping(source = "familyGroupId", target = "familyGroup", qualifiedByName = "mapFamilyGroup")
    @Mapping(source = "familyCourseRegistrationIds", target = "familyCourseRegistrations", qualifiedByName = "mapFamilyCourseRegistrationList")
    @Mapping(source = "loginRequestIds", target = "loginRequests", qualifiedByName = "mapLoginRequestList")
    FamilyMember toEntity(FamilyMemberDto familyMemberDto);

    @Mapping(source = "familyGroup.familyGroupId", target = "familyGroupId")
    @Mapping(source = "familyCourseRegistrations", target = "familyCourseRegistrationIds", qualifiedByName = "mapFamilyCourseRegistration")
    @Mapping(source = "loginRequests", target = "loginRequestIds", qualifiedByName = "mapLoginRequestIds")
    FamilyMemberDto toDto(FamilyMember familyMember);
}
