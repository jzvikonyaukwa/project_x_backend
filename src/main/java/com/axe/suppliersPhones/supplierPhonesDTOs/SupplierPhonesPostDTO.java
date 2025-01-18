package com.axe.suppliersPhones.supplierPhonesDTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SupplierPhonesPostDTO {
    private Long id;
    private Long supplierId;
    private String phone;
    private String label;
    private Boolean delete;
}
