package com.axe.clientAddresses.clientAdressDTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClientAddressPostDTO {
    private Long id;
    private Long clientId;
    private String street;
    private String suburb;
    private String city;
    private String country;
    private String label;
    private Boolean delete;
}
