package com.axe.delivery_notes.delivery_notesDTOs;

import lombok.Data;

import java.util.List;
@Data
public class DeliveryNoteAddItemsDTO {
    private Long deliveryNoteId;
    private List<Long> addManufacturedProductsID;
    private List<Long> addConsumablesOnQuoteID;


}
