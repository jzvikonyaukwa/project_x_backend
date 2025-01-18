package com.axe.suppliersAddresses.supplierAdressDTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SupplierAddressPostDTO {
    private Long id;
    private Long supplierId;
    private String street;
    private String suburb;
    private String city;
    private String country;
    private String label;
    private Boolean delete;
}
