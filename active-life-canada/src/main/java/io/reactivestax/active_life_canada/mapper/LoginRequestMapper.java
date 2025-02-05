package io.reactivestax.active_life_canada.mapper;

import io.reactivestax.active_life_canada.domain.LoginRequest;
import io.reactivestax.active_life_canada.dto.LoginRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoginRequestMapper {

    LoginRequest toEntity(LoginRequestDto loginRequestDto);

    LoginRequestDto toDto(LoginRequest loginRequest);
}
