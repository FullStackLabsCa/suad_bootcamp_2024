package io.reactivestax.activelifecanada.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentIntentRequest {
    private long amount;
    private String currency;
    private String paymentMethodId;
}
