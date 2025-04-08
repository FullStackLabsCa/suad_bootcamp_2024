package io.reactivestax.activelifecanada.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {
    private String cartId;
    private List<OfferedCourseDto> cartItems = new ArrayList<>();
    private Boolean isActive;
    private Long familyMemberId;
    private Long cartCount;
    private Double totalPrice;
}
