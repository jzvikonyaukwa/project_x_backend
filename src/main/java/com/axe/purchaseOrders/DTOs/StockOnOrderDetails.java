package com.axe.purchaseOrders.DTOs;

import java.time.LocalDate;

public interface StockOnOrderDetails {
    Long getPurchaseOrderId();
    LocalDate getExpectedDeliveryDate();
    LocalDate getDateOrdered();
    String getPurchaseOrderStatus();
    Long getSupplierId();
    String getSupplierName();
    Long getProductOnPurchaseOrderId();
    Double getWeightOrdered();
    String getProductStatus();
    Long getSteelSpecificationId();
    String getIsqGrade();
    Integer getWidth();
    String getCoating();
    String getFinish();
    String getColor();
    Double getGauge();
}
