package com.axe.delivery_notes.delivery_notesDTOs;

import lombok.Data;

import java.util.List;

@Data
public class DeliveryNoteRemoveItemsDTO {
    private Long deliveryNoteId;
    private List<Long> removeConsumablesOnQuoteID;
    private List<Long> removeManufacturedProductsID;
}
