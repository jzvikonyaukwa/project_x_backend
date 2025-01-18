package com.axe.clientPhones.clientPhoneDTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClientPhonePostDTO {

    private Long id;
    private String phone;
    private String label;
    private Long clientId;
    private Boolean delete;
}
