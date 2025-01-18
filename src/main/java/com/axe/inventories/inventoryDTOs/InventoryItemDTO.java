package com.axe.inventories.inventoryDTOs;

import java.time.LocalDate;

public interface InventoryItemDTO {

    // INVENTORY ITEM
    Long getInventoryId();
    String getProjectName();
    String getClientName();
    LocalDate getDateIn();
    LocalDate getDateOut();
    Long getDeliveryNoteId();
    Long getQuoteId();

    // SHARED
//    Integer getItemId();
    String getItemType();

    // PRODUCT
    Long getProductId();
    String getProductName();
    String getProductStatus();
    Double getTotalProductLength();
    Integer getNumberOfAggregatedProducts();
    String getFrameName();
    String getFrameType();

    // CONSUMABLLong getCon
    Long getConsumableOnQuoteId();
    String getConsumableName();
    Integer getNumberOfConsumables();

}