package io.reactivestax.activelifecanada.mapper;

import io.reactivestax.activelifecanada.domain.LoginRequest;
import io.reactivestax.activelifecanada.dto.LoginRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoginRequestMapper {

    LoginRequest toEntity(LoginRequestDto loginRequestDto);

    LoginRequestDto toDto(LoginRequest loginRequest);
}
