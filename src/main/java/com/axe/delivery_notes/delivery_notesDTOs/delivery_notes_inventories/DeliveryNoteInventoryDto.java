package com.axe.delivery_notes.delivery_notesDTOs.delivery_notes_inventories;

import java.math.BigDecimal;

public interface DeliveryNoteInventoryDto {
        Long getDeliveryNoteId();
        String getDateCreated();
        String getDateDelivered();
        String getDeliveryAddress();

        String getStatus();
        Long getInventoryId();
        String getInventoryDateIn();
        String getInventoryDateOut();
        String getFrameType();
        String getFrameName();
        Double getTotalLength();
        String getCode();
        Integer getQuantity();
        String getName();
        BigDecimal getPrice();
        Long getConsumableId();
        Long getManufacturedQuantity();
        Long getManufacturedId();
        String getProductName();

}
