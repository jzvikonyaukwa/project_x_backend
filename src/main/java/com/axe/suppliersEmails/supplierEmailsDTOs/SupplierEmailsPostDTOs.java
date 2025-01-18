package com.axe.suppliersEmails.supplierEmailsDTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SupplierEmailsPostDTOs {

    private Long id;
    private String email;
    private String label;
    private Long supplierId;
    private Boolean delete;
}
