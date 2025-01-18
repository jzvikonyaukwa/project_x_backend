package com.axe.consumables.DTOs;

public interface ConsumableLowStockData {
    Long getConsumableInWarehouseId();
    Integer getQty();
    String getName();
    String getSerialNumber();
    String getUom();
    String getWarehouseName();
    String getCategory();
    String getSourceCountry();
}
