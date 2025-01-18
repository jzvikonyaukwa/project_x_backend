package com.axe.purchaseOrders.DTOs;

import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

public interface ProductsOnOrderForSupplierDTO {
    Long getPurchaseOrderId();
    Long getProductOnPurchaseOrderId();
    BigDecimal getWeightOrdered();
    Long getSteelSpecificationId();
    String getIsqGrade();
    Double getWidth();
    String getCoating();
    String getFinish();
    String getColor();
    Double getGauge();
    BigDecimal getPurchaseCostPerKg();

    //CONSUMABLES
    Long getConsumableId();
    String getProductName();
    BigDecimal getCostPerUnit();
    Integer getQtyOrdered();

    Long getConsumableOnPurchaseOrderId();
}
