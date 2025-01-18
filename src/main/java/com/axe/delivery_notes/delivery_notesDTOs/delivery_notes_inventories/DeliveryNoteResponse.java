package com.axe.delivery_notes.delivery_notesDTOs.delivery_notes_inventories;

import com.axe.delivery_notes.Status;
import lombok.Data;

import java.util.List;

@Data
public class DeliveryNoteResponse {
    private Long id;
    private String dateCreated;
    private String dateDelivered;
    private String deliveryAddress;
    private Status status;
    private List<InventoryResponse> inventories;
}
