package com.axe.purchaseOrders.DTOs;

import com.axe.grvs.GRV;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDelivered {
    Long purchaseOrderId;
    Long productOnPurchaseOrderId;
    Long consumableOnPurchaseOrderId;
    Float weightDelivered;
    Long grvId;
}
