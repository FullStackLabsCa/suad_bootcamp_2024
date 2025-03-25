package io.reactivestax.types.dto;

import io.reactivestax.types.enums.Direction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Trade {
    private String tradeIdentifier;
    private String tradeDateTime;
    private String accountNumber;
    private String cusip;
    private Direction direction;
    private Integer quantity;
    private Double price;
    private Integer position;
}
