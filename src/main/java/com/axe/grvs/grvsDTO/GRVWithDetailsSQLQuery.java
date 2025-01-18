package com.axe.grvs.grvsDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface GRVWithDetailsSQLQuery {

    //GRV
    Long getGrvId();
    LocalDate getDateReceived();
    String getGrvComments();
    String getSupplierGrvCode();

    //Steel Coil Supplier related fields
    Long getSteelCoilSupplierId();
    String getSteelCoilSupplierName();

    // Steel Coil related fields
    String getStatus();
    Long getSteelCoilId();
    String getCoilNumber();
    String getCardNumber();
    BigDecimal getEstimatedMeterRunOnArrival();
    BigDecimal getWeightInKgsOnArrival();
    BigDecimal getEstimatedMetersRemaining();
    BigDecimal getLandedCostPerMtr();
    String getIsqGrade();

    // Width, Color, Finish, Coating, and Gauge fields
    String getFinish();
    String getColor();
    BigDecimal getWidth();
    String getCoating();
    BigDecimal getGauge();

    // Steel Coil Warehouse related fields
    Long getSteelCoilWarehouseId();
    String getSteelCoilWarehouse();

    //Consumables
    Long getConsumableOnGrvId();
    Long getConsumableInWarehouseId();
    String getConsumableName();
    Long getConsumableId();
    String getSerialNumber();
    String getUom();
    Long getConsumableCategoryId();
    BigDecimal getAvgLandedPrice();

    // Consumable Supplier related fields
    Long getConsumableSupplierId();
    String getConsumableSupplierName();

    // Consumable Warehouse related fields
    Long getConsumableWarehouseId();
    String getConsumableWarehouse();

    Integer getQtyOrdered();
    BigDecimal getLandedPrice();

    // Purchase Order ID
    Long getPurchaseOrderId();
}
