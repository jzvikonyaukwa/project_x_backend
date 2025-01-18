package com.axe.consumablesInWarehouse;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consumables-in-warehouse")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class ConsumablesInWarehouseController {
    private final ConsumableInWarehouseService consumableInWarehouseService;

    public ConsumablesInWarehouseController(ConsumableInWarehouseService consumableInWarehouseService) {
        this.consumableInWarehouseService = consumableInWarehouseService;
    }

    @GetMapping("warehouse/{warehouseId}")
    public List<ConsumablesInWarehouse> getAllConsumablesInWarehouse(@PathVariable Long warehouseId){
        return consumableInWarehouseService.getAllConsumablesInWarehouse(warehouseId);
    }
}
