package com.axe.aggregated_products.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AggregatedProductDTO {
    private Long id;
    private String stick;
    private String stickType;
    private String code;
    private BigDecimal length;
    private String status;
}
