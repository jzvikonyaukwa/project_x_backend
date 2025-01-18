package com.axe.inventories.inventoryDTOs;

import lombok.Data;

import java.util.List;

@Data
public class GroupMPs {
    private Long quoteId;
    private Long cuttingListId;
    private String frameType;
    private String frameName;
    private int totalItems;
    private double totalLengthOrQty;
    private List<InventoryItemDTO> groupedInventoryItemDTO;
}
