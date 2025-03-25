package io.reactivestax.activelifecanada.dto.ems;

import lombok.Data;

@Data
public class PhoneDTO {
    private Long id;
    private Long clientId;
    private String outgoingPhoneNumber;
}
