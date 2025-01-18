package com.axe.consumablesInWarehouse;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConsumableInWarehouseRepository extends JpaRepository<ConsumablesInWarehouse, Long> {

    @Query("SELECT c FROM ConsumablesInWarehouse c WHERE c.consumable.id = ?1 AND c.warehouse.id = ?2")
    Optional<ConsumablesInWarehouse> findByConsumableIdAndWarehouseId(Long consumableId, Long warehouseId);

    @Query("SELECT c FROM ConsumablesInWarehouse c WHERE c.warehouse.id = ?1 ORDER BY c.consumable.name ASC")
    List<ConsumablesInWarehouse> getAllConsumablesInWarehouse(Long warehouseId);
}
