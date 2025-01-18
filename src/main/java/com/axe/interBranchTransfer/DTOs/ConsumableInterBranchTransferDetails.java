package com.axe.interBranchTransfer.DTOs;
import java.time.LocalDate;

public interface ConsumableInterBranchTransferDetails {

    // InterBranchTransfer
    Long getInterBranchTransferId();
    LocalDate getDateTransferred();
    Integer getQtyTransferred();

    // ConsumableInWarehouseFrom
    Long getConsumableInWarehouseFromId();
    Long getConsumableInWarehouseFromAvgLandedPrice();
    Integer getConsumableInWarehouseFromQty();


    // ConsumableInWarehouseTo
    Long getConsumableInWarehouseToId();
    Long getConsumableInWarehouseToAvgLandedPrice();
    Integer getConsumableInWarehouseToQty();

    // Consumable
    Long getConsumableId();
    String getConsumableName();
    String getSerialNumber();



}
