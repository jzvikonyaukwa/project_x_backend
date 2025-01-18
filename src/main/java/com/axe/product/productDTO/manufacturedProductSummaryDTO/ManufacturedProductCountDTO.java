package com.axe.product.productDTO.manufacturedProductSummaryDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ManufacturedProductCountDTO {
    private String productType;
    private Long count;

}
