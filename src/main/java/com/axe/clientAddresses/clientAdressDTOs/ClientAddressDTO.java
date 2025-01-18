package com.axe.clientAddresses.clientAdressDTOs;


public interface ClientAddressDTO {
    Long getId();
    Long getClientId();
    String getStreet();
    String getSuburb();
    String getCity();
    String getCountry();
    String getLabel();
}
