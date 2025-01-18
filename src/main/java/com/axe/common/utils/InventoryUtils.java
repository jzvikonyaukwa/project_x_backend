package com.axe.common.utils;

import com.axe.inventories.Inventory;
import com.axe.product.Product;

import java.time.LocalDateTime;

public class InventoryUtils {
    public static Inventory  createInventoryItem(Product product, LocalDateTime dateTime) {
        if (product.getStatus().equals("completed")) {
            Inventory inventory = Inventory.builder()
                    .dateIn(dateTime.toLocalDate())
                    .build();

            product.setInventory(inventory);
            inventory.setProduct(product);
            return inventory;
        }
        return null;
    }
}