package io.reactivestax.active_life_canada.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodRequestDto {
    private String cardNumber;
    private String expMonth;
    private String expYear;
    private String cvc;
}
