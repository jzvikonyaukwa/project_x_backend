package com.axe.consumables.DTOs;

import com.axe.consumables.Consumable;
import com.axe.warehouse.Warehouse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class ConsumablePostDTO {

//    Long consumableOnPurchaseOrderId;
//    Long purchaseOrderId;
    Long id; // Add this field if "id" is relevant
    Long consumableOnGrvId;
    Long consumableInWarehouseId;
    Consumable consumable;
    Integer qtyOrdered;
    BigDecimal landedPrice;
    Warehouse warehouse;

}
