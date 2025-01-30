package io.reactivestax.active_life_canada.dto.ems;

import lombok.Data;


@Data
public class SmsDTO {
    private Long id;
//    private Long clientId;
    private String phone;
    private String message;
}
