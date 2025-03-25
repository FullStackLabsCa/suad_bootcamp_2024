package io.reactivestax.activelifecanada.mapper;

import io.reactivestax.activelifecanada.domain.OfferedCourseFee;
import io.reactivestax.activelifecanada.dto.OfferedCourseFeeDto;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface OfferedCourseFeeMapper {

    OfferedCourseFee toEntity(OfferedCourseFeeDto offeredCourseFeeDto);

    OfferedCourseFeeDto toDto(OfferedCourseFee offeredCourseFee);
}
