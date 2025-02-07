package io.reactivestax.active_life_canada.mapper;

import io.reactivestax.active_life_canada.domain.Cart;
import io.reactivestax.active_life_canada.domain.OfferedCourse;
import io.reactivestax.active_life_canada.dto.CartDto;
import io.reactivestax.active_life_canada.dto.OfferedCourseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OfferedCourseMapperHelper.class})
public interface CartMapper {

    Cart toEntity(CartDto cartDto);

    CartDto toDto(Cart cart);
}
