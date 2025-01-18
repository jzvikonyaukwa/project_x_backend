package com.axe.product.productDTO.manufacturedProductSummaryDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyManufacturedProductCountDTO {
    private int month;
    private String productType;
    private Long count;
}