package com.axe.consumablesInWarehouse;


import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ConsumableInWarehouseService {
    private final ConsumableInWarehouseRepository consumableInWarehouseRepository;

    public ConsumableInWarehouseService(ConsumableInWarehouseRepository consumableInWarehouseRepository) {
        this.consumableInWarehouseRepository = consumableInWarehouseRepository;
    }
    public ConsumablesInWarehouse saveConsumableInWarehouse(ConsumablesInWarehouse consumableInWarehouse) {
        return consumableInWarehouseRepository.save(consumableInWarehouse);
    }

    public List<ConsumablesInWarehouse> getAllConsumablesInWarehouse(Long warehouseId) {
        return consumableInWarehouseRepository.getAllConsumablesInWarehouse(warehouseId);
    }

    public ConsumablesInWarehouse addConsumableInWarehouse(ConsumablesInWarehouse consumablesInWarehouse) {
        return consumableInWarehouseRepository.save(consumablesInWarehouse);
    }


    public ConsumablesInWarehouse updateQuantity(Long consumableId, Long warehouseId, Integer quantity) {
        ConsumablesInWarehouse consumablesInWarehouse
                = consumableInWarehouseRepository.findByConsumableIdAndWarehouseId(consumableId, warehouseId)
                .orElseThrow(() -> new RuntimeException("Consumable with id [%s] not found"
                        .formatted(consumableId)))
                ;

        consumablesInWarehouse.setQty(consumablesInWarehouse.getQty() + quantity);

        return consumableInWarehouseRepository.save(consumablesInWarehouse);
    }
}
