package com.axe.productTransactions.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ProductTransactionDetails {
    Long getId();
    LocalDate getDate();
    Long getManufacturedProductId();
    Long getProductId();
    String getManufacturedProductCode();
    String getFrameName();
    BigDecimal getLength();
    Long getSteelCoilId();
    String getCoilNumber();
    Long getWastageId();
    BigDecimal getMtrsWasted();
    Long getStockOnHandId();
    BigDecimal getStockOnHandLength();

    Double getSteelCoilWidth();
     Double getGauge();
     String getColor();
     String  getProductType();
}
