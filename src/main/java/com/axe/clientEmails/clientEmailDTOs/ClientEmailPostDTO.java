package com.axe.clientEmails.clientEmailDTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientEmailPostDTO {
    private Long id;
    private Long clientId;
    private String email;
    private String label;
    private Boolean delete;
}
