package com.axe.stockOnHand.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductForStockOnHandDTO {
    String productName;
    String stick;
    String stickType;
    BigDecimal length;
    Long steelCoilId;
}
