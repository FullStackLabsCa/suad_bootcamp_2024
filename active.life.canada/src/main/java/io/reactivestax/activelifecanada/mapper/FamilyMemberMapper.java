package io.reactivestax.activelifecanada.mapper;

import io.reactivestax.activelifecanada.domain.FamilyMember;
import io.reactivestax.activelifecanada.dto.FamilyMemberDto;
import io.reactivestax.activelifecanada.dto.SignUpDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {FamilyMemberMapperHelper.class})
public interface FamilyMemberMapper {

//    @Mapping(source = "familyGroupId", target = "familyGroup", qualifiedByName = "mapFamilyGroup")
//    @Mapping(source = "familyCourseRegistrationIds", target = "familyCourseRegistrations", qualifiedByName = "mapFamilyCourseRegistrationList")
//    @Mapping(source = "loginRequestIds", target = "loginRequests", qualifiedByName = "mapLoginRequestList")
    FamilyMember toEntity(FamilyMemberDto familyMemberDto);

//    @Mapping(source = "familyGroup.familyGroupId", target = "familyGroupId")
//    @Mapping(source = "familyCourseRegistrations", target = "familyCourseRegistrationIds", qualifiedByName = "mapFamilyCourseRegistration")
//    @Mapping(source = "loginRequests", target = "loginRequestIds", qualifiedByName = "mapLoginRequestIds")
    FamilyMemberDto toDto(FamilyMember familyMember);

    FamilyMemberDto toFamilyMemberDto(SignUpDto signUpDto);

    FamilyMember toFamilyMember(SignUpDto signUpDto);
}
