package com.axe.inventories;

import com.axe.delivery_notes.DeliveryNote;
import com.axe.inventories.exceptions.ResourceNotFoundException;
import com.axe.inventories.inventoryDTOs.InventoryItemDTO;
import com.axe.returned_products.ReturnedProducts;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = {"http://localhost:4200", "https://axebuild.io", "https://axebuild.io"})
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("")
    public List<Inventory> getAllInventories() {
        return inventoryService.getAllInventories();
    }

    @GetMapping("/items-in-stock")
    public List<Inventory> getAllInventoryItemsInStock() {
        return inventoryService.getAllInventoryItemsInStock();
    }

    @GetMapping("/items-with-details")
    public List<InventoryItemDTO> getAllInventoriesWithDetails() {
        return inventoryService.getAllInventoriesWithDetails();
    }

    @GetMapping("/items-for-project/{projectId}")
    public List<InventoryItemDTO> getProjectInventoryItemsInStock(@PathVariable Long projectId) {
        return inventoryService.getProjectInventoryItemsInStock(projectId);
    }

    @GetMapping("/items-for-credit-note-project/{projectId}")
    public List<InventoryItemDTO> getAllItemsForCreditNoteProject(@PathVariable Long projectId) {
        return inventoryService.getAllItemsForCreditNoteProject(projectId);
    }

    // To Get Available Inventories even of returned Items
    @GetMapping("/available-inventory")
    public List<Inventory> getAvailableInventories() {
        return inventoryService.getAvailableInventories();
    }

    @GetMapping("/items-in-stock/{projectId}")
    public List<Inventory> getAllInventoryItemsInStockByProjectId(@PathVariable Long projectId) {
        return inventoryService.getAllInventoryItemsInStockByProjectId(projectId);
    }

    @PostMapping
    public Inventory createInventory(@RequestBody Inventory inventory) {
        return inventoryService.createInventory(inventory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventory> updateInventory(@PathVariable Long id, @RequestBody Inventory inventory) {
        try {
            Inventory updatedInventory = inventoryService.updateInventory(id, inventory);
            return ResponseEntity.ok(updatedInventory);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{inventoryId}/returnedProducts")
    public ResponseEntity<Inventory> addReturnedProduct(@PathVariable Long inventoryId, @RequestBody ReturnedProducts returnedProduct) {
        try {
            Inventory updatedInventory = inventoryService.addReturnedProduct(inventoryId, returnedProduct);
            return ResponseEntity.ok(updatedInventory);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(409).build();
        }
    }

    @PostMapping("/{inventoryId}/deliveryNote")
    public ResponseEntity<Inventory> associateDeliveryNoteWithInventory(@PathVariable Long inventoryId, @RequestBody DeliveryNote deliveryNote) {
        try {
            Inventory updatedInventory = inventoryService.associateDeliveryNoteWithInventory(inventoryId, deliveryNote);
            return ResponseEntity.ok(updatedInventory);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(409).build();
        }
    }


}
