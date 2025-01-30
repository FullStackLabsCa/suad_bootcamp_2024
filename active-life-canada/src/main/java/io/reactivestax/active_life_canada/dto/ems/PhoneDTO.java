package io.reactivestax.dto;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class PhoneDTO {
    private Long id;
    private Long clientId;
    private String outgoingPhoneNumber;
}
