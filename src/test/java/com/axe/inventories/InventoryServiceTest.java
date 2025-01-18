package com.axe.inventories;


import com.axe.inventories.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InventoryServiceTest {
//
//    @Mock
//    private InventoriesRepository inventoriesRepository;
//
//    @InjectMocks
//    private InventoryService inventoryService;
//
//    private Inventory existingInventory;
//    private Inventory updatedInventory;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        existingInventory = new Inventory();
//        existingInventory.setId(1L);
//        existingInventory.setDateIn(LocalDate.parse("2024-05-01"));
//        existingInventory.setDateOut(LocalDate.parse("2024-05-10"));
//
//
//        updatedInventory = new Inventory();
//        updatedInventory.setDateIn(LocalDate.parse("2024-06-01"));
//        updatedInventory.setDateOut(LocalDate.parse("2024-06-10"));
//    }
//
//    @Test
//    void testUpdateInventory() {
//        when(inventoriesRepository.findById(1L)).thenReturn(Optional.of(existingInventory));
//        when(inventoriesRepository.save(any(Inventory.class))).thenReturn(existingInventory);
//
//        Inventory result = inventoryService.updateInventory(1L, updatedInventory);
//
//        assertNotNull(result);
//        assertEquals(updatedInventory.getDateIn(), result.getDateIn());
//        assertEquals(updatedInventory.getDateOut(), result.getDateOut());
////        assertEquals(updatedInventory.getConsumableOnQuotes(), result.getConsumableOnQuotes());
////        assertEquals(updatedInventory.getManufacturedProducts(), result.getManufacturedProducts());
//        verify(inventoriesRepository, times(1)).findById(1L);
//        verify(inventoriesRepository, times(1)).save(existingInventory);
//    }
//
//    @Test
//    void testUpdateInventory_NotFound() {
//        when(inventoriesRepository.findById(1L)).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
//            inventoryService.updateInventory(1L, updatedInventory);
//        });
//
//        assertEquals("Inventory not found with id 1", exception.getMessage());
//        verify(inventoriesRepository, times(1)).findById(1L);
//        verify(inventoriesRepository, times(0)).save(any(Inventory.class));
//    }
}
