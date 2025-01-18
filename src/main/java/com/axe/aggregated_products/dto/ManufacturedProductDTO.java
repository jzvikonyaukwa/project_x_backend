package com.axe.aggregated_products.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ManufacturedProductDTO {

    private Long id;
    private String frameName;
    private String frameType;
    private BigDecimal totalLength;
    private Integer totalQuantity;
    private String status;
    private List<AggregatedProductDTO> aggregatedManufacturedProducts;
}
