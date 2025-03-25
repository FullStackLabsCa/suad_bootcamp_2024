package io.reactivestax.dto;

import lombok.Data;


@Data
public class SmsDTO {
    private Long id;
    private Long clientId;
    private String phone;
    private String message;
}
