package io.reactivestax.active_life_canada.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {

    private Long cartId;
    private List<Long> offeredCourseIds = new ArrayList<>();
    private Boolean isActive;
}
