package com.axe.consumables.DTOs;

import java.math.BigDecimal;

public interface ConsumableDetailsSQL {
    Long getConsumableId();
    Long getConsumableInWarehouseId();
    String getSerialNumber();
    String getName();
    BigDecimal getAvgLandedPrice();
    String getUom();
    Integer getQty();
//    Integer getMinAlertQty();

    String getCategory();
    String getSourceCountry();

    //Warehouse
    String getWarehouseName();
    Long getWarehouseId();
}
