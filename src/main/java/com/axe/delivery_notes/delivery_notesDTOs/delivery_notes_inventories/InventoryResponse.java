package com.axe.delivery_notes.delivery_notesDTOs.delivery_notes_inventories;

import com.axe.consumablesOnQuote.ConsumableOnQuote;
import lombok.Data;

@Data
public class InventoryResponse {
    private Long id;
    private String dateIn;
    private String dateOut;
    private ConsumableOnQuote consumable;
    private ProductResponse product;
}