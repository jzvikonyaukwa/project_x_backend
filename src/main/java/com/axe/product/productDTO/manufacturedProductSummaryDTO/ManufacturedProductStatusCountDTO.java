package com.axe.product.productDTO.manufacturedProductSummaryDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ManufacturedProductStatusCountDTO {
    private String status;
    private Long count;
}
