package com.axe.delivery_notes.delivery_notesDTOs;

import com.axe.delivery_notes.Status;
import com.axe.inventories.Inventory;

import java.time.LocalDate;
import java.util.List;

public record DeliveryNotesRequest(LocalDate dateCreated,
                                   LocalDate dateDelivered,
                                   String deliveryAddress,
                                   Status status,
                                   Integer projectId ,
                                   List<Inventory> inventories)
        {
}
