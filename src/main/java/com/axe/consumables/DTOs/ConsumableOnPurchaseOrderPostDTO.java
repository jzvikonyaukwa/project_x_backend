package com.axe.consumables.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ConsumableOnPurchaseOrderPostDTO {
    Long consumableOnPurchaseOrderId;
    Long consumableId;
    Integer qty;
    BigDecimal costPerUnit;
}
