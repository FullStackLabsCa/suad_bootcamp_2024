package io.reactivestax.active_life_canada.mapper;

import io.reactivestax.active_life_canada.domain.FamilyCourseRegistration;
import io.reactivestax.active_life_canada.domain.FamilyGroup;
import io.reactivestax.active_life_canada.domain.LoginRequest;
import io.reactivestax.active_life_canada.repository.FamilyCourseRegistrationRepository;
import io.reactivestax.active_life_canada.repository.FamilyGroupRepository;
import io.reactivestax.active_life_canada.repository.LoginRequestRepository;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FamilyMemberMapperHelper {

    @Autowired
    private FamilyGroupRepository familyGroupRepository;

    @Autowired
    private FamilyCourseRegistrationRepository familyCourseRegistrationRepository;

    @Autowired
    private LoginRequestRepository loginRequestRepository;

    @Named("mapFamilyGroup")
    public FamilyGroup mapFamilyGroup(Long familyGroupId) {
        return familyGroupId != null ? familyGroupRepository.findById(familyGroupId).orElse(null) : null;
    }

    @Named("mapFamilyCourseRegistration")
    public List<Long> mapFamilyCourseRegistration(List<FamilyCourseRegistration> registrations) {
        return registrations != null ? registrations.stream()
                .map(FamilyCourseRegistration::getFamilyCourseRegistrationId)
                .collect(Collectors.toList()) : null;
    }

    @Named("mapFamilyCourseRegistrationList")
    public List<FamilyCourseRegistration> mapFamilyCourseRegistrationList(List<Long> registrationIds) {
        return registrationIds != null ? familyCourseRegistrationRepository.findAllById(registrationIds) : null;
    }

    @Named("mapLoginRequestIds")
    public List<Long> mapLoginRequestIds(List<LoginRequest> loginRequests) {
        return loginRequests != null ? loginRequests.stream()
                .map(LoginRequest::getLoginRequestId)
                .collect(Collectors.toList()) : null;
    }

    @Named("mapLoginRequestList")
    public List<LoginRequest> mapLoginRequestList(List<Long> loginRequestIds) {
        return loginRequestIds != null ? loginRequestRepository.findAllById(loginRequestIds) : null;
    }
}
