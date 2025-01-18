package com.axe.consumables;

import com.axe.consumables.DTOs.ConsumableDetails;
import com.axe.consumables.DTOs.ConsumableLowStockData;
import com.axe.consumables.DTOs.ConsumablesStockLevelToBeChecked;
import com.axe.consumables.DTOs.InventoryBalanceHistory;
import com.axe.consumablesOnPurchaseOrder.dtos.ConsumablesOnPurchaseOrder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/consumables")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class ConsumablesController {
    private final static Logger logger = LoggerFactory.getLogger(ConsumablesController.class);

    private final ConsumablesService consumablesService;

    public ConsumablesController(ConsumablesService consumablesService) {
        this.consumablesService = consumablesService;
    }

    @GetMapping("")
    public List<Consumable> getAllConsumables(){
        return consumablesService.getAllConsumables();
    }

//    @GetMapping("warehouse/{warehouseId}")
//    public List<ConsumableDetailsSQL> getAllConsumablesInWarehouse(@PathVariable Long warehouseId){
//        return consumablesService.getAllConsumablesInWarehouse(warehouseId);
//    }

    @GetMapping("{id}")
    public Consumable getConsumableById(@PathVariable Long id){
        return consumablesService.getConsumableById(id);
    }

    @GetMapping("consumables-on-order")
    public List<ConsumablesOnPurchaseOrder> getConsumablesOnOrder(){
        return consumablesService.getConsumablesOnOrder();
    }

    @GetMapping("details")
    public List<Consumable> getAllConsumableDetails(){
        return consumablesService.getAllConsumableDetails();
    }

    @GetMapping("low-stock")
    public List<ConsumableLowStockData> getLowStockConsumables(){
        return consumablesService.getLowStockConsumables();
    }

    @PostMapping("check-stock-level")
    public List<ConsumablesStockLevelToBeChecked> checkStockLevels(@RequestBody List<ConsumablesStockLevelToBeChecked> consumablesToBeChecked){
        return consumablesService.checkConsumableStockLevels(consumablesToBeChecked);
    }

    @PostMapping("")
    public Consumable createConsumable(@RequestBody Consumable consumable){
        return consumablesService.saveConsumable(consumable);
    }

    @PutMapping("update")
    public Consumable updateConsumable(@RequestBody Consumable consumable){
        return consumablesService.updateConsumable(consumable);
    }

    @GetMapping("{warehouseID}/history/{consumableID}")
    @Operation(summary = "Get inventory balance history for a consumable in a warehouse")
    public List<InventoryBalanceHistory> inventoryBalanceHistory(
            @RequestParam(value = "startDate", required = false) String startDateStr
            , @Parameter(description = "ID of the warehouse", example = "87654321") // 
            @PathVariable Long warehouseID
            , @Parameter(description = "ID of the consumable", example = "87654321") //
            @PathVariable Long consumableID) {
        logger.info("Fetching inventory balance history for consumableId: {} in warehouseID: {}", consumableID, warehouseID);
        
        // if (warehouseID == null || consumableID == null) {
        //     throw new IllegalArgumentException("warehouseID and consumableID must not be null.");
        // }

        try {
            // If startDateStr is not provided, default to the first day of the month, 6 months ago.
            LocalDate startDate = (startDateStr != null) ? LocalDate.parse(startDateStr)
                    : LocalDate.now().withDayOfMonth(1).minusMonths(6);

            return consumablesService.inventoryBalanceHistory(warehouseID, consumableID, startDate);
        } catch (Exception exception) {
            throw new RuntimeException(
                    "Server encountered an error while fetching inventory balance history for consumableID: "
                            + consumableID + " in warehouseID: " + warehouseID);
        }
    }

    @GetMapping("{consumableID}/details")
    @Operation(summary = "Get basic details of a consumable")
    public ConsumableDetails getConsumableDetails(
            @Parameter(description = "ID of the consumable", example = "87654321") //
            @PathVariable Long consumableID) {
        logger.info("Fetching consumable details for consumableId: {}", consumableID);
        try {
            return consumablesService.fetchConsumableDetails(consumableID);
        } catch (Exception exception) {
            throw new RuntimeException(
                    "Server encountered an error while fetching consumable details for consumableID: " + consumableID);
        }
    }
}
