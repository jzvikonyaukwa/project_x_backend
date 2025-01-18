package com.axe.inventories;

import com.axe.aggregated_products.AggregatedProductsService;
import com.axe.delivery_notes.DeliveryNote;
import com.axe.inventories.exceptions.ResourceNotFoundException;
import com.axe.inventories.inventoryDTOs.InventoryItemDTO;
import com.axe.returned_products.ReturnedProducts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InventoryService {
    Logger logger = LoggerFactory.getLogger(AggregatedProductsService.class);

    private final InventoriesRepository inventoriesRepository;

    public InventoryService(InventoriesRepository inventoriesRepository) {
        this.inventoriesRepository = inventoriesRepository;
    }

    public Optional<Inventory> getInventoryById(Long id) {
        return inventoriesRepository.findById(id);
    }


    public List<Inventory> getAllInventories() {
        return inventoriesRepository.findAll();
    }

    public Inventory createInventory(Inventory inventory) {
        return  inventoriesRepository.save(inventory);

    }

    public Inventory updateInventory(Long id, Inventory inventory) {
        return inventoriesRepository.findById(id)
                .map(existingInventory -> {
                    existingInventory.setDateIn(inventory.getDateIn());
                    existingInventory.setDateOut(inventory.getDateOut());
//                    existingInventory.setConsumableOnQuote(inventory.getConsumableOnQuote());
                    existingInventory.setProduct(inventory.getProduct());
//                    existingInventory.setReturnedProducts(inventory.getReturnedProducts());
                    existingInventory.setDeliveryNote(inventory.getDeliveryNote());
                    return inventoriesRepository.save(existingInventory);
                })
                .orElseThrow(() -> new ResourceNotFoundException(409,"Inventory not found with id " + id));
    }

    public List<Inventory> getInventoriesByIds(List<Long> ids) {
        return inventoriesRepository.findByIdIn(ids);
    }


    public Inventory addReturnedProduct(Long inventoryId, ReturnedProducts returnedProduct) {
        //                   inventory.getReturnedProducts().add(returnedProduct);
        //                   returnedProduct.setInventory(inventory);
        return inventoriesRepository.findById(inventoryId)
                .map(inventoriesRepository::save)
                .orElseThrow(() -> new ResourceNotFoundException(409, "Inventory not found with id " + inventoryId));
    }

    public Inventory associateDeliveryNoteWithInventory(Long inventoryId, DeliveryNote deliveryNote) {
        return inventoriesRepository.findById(inventoryId)
                .map(inventory -> {
                    inventory.setDeliveryNote(deliveryNote);
                    return inventoriesRepository.save(inventory);
                })
                .orElseThrow(() -> new ResourceNotFoundException(409, "Inventory not found with id " + inventoryId));
    }

    public List<InventoryItemDTO> getAllInventoriesWithDetails() {
        return inventoriesRepository.findAllWithDetails();
    }

    public List<InventoryItemDTO> getProjectInventoryItemsInStock(Long projectId) {
        Set<Long> seenProductIds = new HashSet<>();
        Set<Long> seenConsumableIds = new HashSet<>();

        return inventoriesRepository.getAllItemsForProject(projectId).stream()
                .filter(inventoryItemDTO -> {
                    // Item must have at least one valid ID
                    if (inventoryItemDTO.getProductId() == null &&
                            inventoryItemDTO.getConsumableOnQuoteId() == null) {
                        return false;
                    }

                    // Handle product items
                    if (inventoryItemDTO.getProductId() != null) {
                        return seenProductIds.add(inventoryItemDTO.getProductId());
                    }

                    // Handle consumable items
                    return seenConsumableIds.add(inventoryItemDTO.getConsumableOnQuoteId());
                })
                .toList();
    }

    public List<InventoryItemDTO> getAllItemsForCreditNoteProject(Long projectId) {
        return inventoriesRepository.getAllItemsForCreditNoteProject(projectId);
    }


    public List<Inventory> getAllInventoryItemsInStock() {
        return inventoriesRepository.findByDeliveryNoteIsNull();
    }


    public void saveAll(List<Inventory> inventories) {
        inventoriesRepository.saveAll(inventories);
    }

    public List<Inventory> getAvailableInventories() {
        return inventoriesRepository.findAvailableInventories();

    }

    public List<Inventory> getAllInventoryItemsInStockByProjectId(Long projectId) {
        return inventoriesRepository.findAvailableInventoriesInStockByProjectId(projectId);
    }
}

