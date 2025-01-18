package com.axe.interBranchTransfer.consumables;

import com.axe.consumables.CalculateWeightedAvgPriceService;
import com.axe.consumables.Consumable;
import com.axe.consumablesInWarehouse.ConsumableInWarehouseRepository;
import com.axe.consumablesInWarehouse.ConsumablesInWarehouse;
import com.axe.interBranchTransfer.DTOs.ConsumableInterBranchTransferDTO;
import com.axe.warehouse.Warehouse;
import com.axe.warehouse.WarehouseRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class ConsumableInterBranchTransferService {

    private final ConsumableInterBranchTransferRepository consumableInterBranchTransferRepository;
    private final WarehouseRepository warehouseRepository;
    private final ConsumableInWarehouseRepository consumableInWarehouseRepository;

    private final Logger logger = LoggerFactory.getLogger(ConsumableInterBranchTransferService.class);
    private final CalculateWeightedAvgPriceService calculateWeightedAvgPriceService;
    public ConsumableInterBranchTransferService(ConsumableInterBranchTransferRepository consumableInterBranchTransferRepository,
                                                WarehouseRepository warehouseRepository,
                                                CalculateWeightedAvgPriceService calculateWeightedAvgPriceService,
                                                ConsumableInWarehouseRepository consumableInWarehouseRepository) {
        this.consumableInterBranchTransferRepository = consumableInterBranchTransferRepository;
        this.warehouseRepository = warehouseRepository;
        this.calculateWeightedAvgPriceService = calculateWeightedAvgPriceService;
        this.consumableInWarehouseRepository = consumableInWarehouseRepository;
    }

    @Transactional
    public ConsumableInterBranchTransfer createConsumableInterBranchTransfer(ConsumableInterBranchTransferDTO interBranchTransfer) {
        logger.info("Inside createConsumableInterBranchTransfer: interBranchTransfer = {}", interBranchTransfer);

        ConsumablesInWarehouse consumablesInWarehouseFrom
                = consumableInWarehouseRepository.findById(interBranchTransfer.consumableInWarehouseFromId())
                .orElseThrow( () ->  new RuntimeException("Steel coil with id [%s] not found"
                        .formatted(interBranchTransfer.consumableInWarehouseFromId())));



        Warehouse warehouse = warehouseRepository.getWarehouseByName("owned");

        Consumable consumable = consumablesInWarehouseFrom.getConsumable();
        logger.info("consumable = {} consumable.getId() = {}", consumable.getName(), consumable.getId());
        ConsumablesInWarehouse consumablesInWarehouseTo
                = consumableInWarehouseRepository
                .findByConsumableIdAndWarehouseId(consumablesInWarehouseFrom.getConsumable().getId(), warehouse.getId())
                .map(consumablesInWarehouse ->
                {
              logger.info("consumablesInWarehouseTo is not null. Adding to qty and updating avg price");
              BigDecimal newAvgLandCostPrice = calculateWeightedAvgPriceService.calculateNewWeightedAverage(
                    consumablesInWarehouseFrom.getAvgLandedPrice(), new BigDecimal(consumablesInWarehouseFrom.getQty()),
                    consumablesInWarehouse.getAvgLandedPrice(), new BigDecimal((consumablesInWarehouse.getQty())));

            consumablesInWarehouse.setAvgLandedPrice(newAvgLandCostPrice);
            consumablesInWarehouse.setQty(consumablesInWarehouse.getQty() + interBranchTransfer.qty());
            logger.info("consumablesInWarehouseTo.getAvgLandedPrice(): {}", consumablesInWarehouse.getAvgLandedPrice());
            return consumablesInWarehouse;

        }).orElseGet(()->{
            logger.info("consumablesInWarehouseTo is null. Creating new consumablesInWarehouseTo");

            return   ConsumablesInWarehouse.builder()
                    .consumable(consumable)
                    .warehouse(warehouse)
                    .avgLandedPrice(consumablesInWarehouseFrom.getAvgLandedPrice())
                    .qty(interBranchTransfer.qty())
                    .build();
        });


        consumableInWarehouseRepository.save(consumablesInWarehouseTo);
        logger.info("Successfully saved the new consumablesInWarehouseTo");
        logger.info("ConsumablesInWarehouseFrom.getNumberOfConsumables() = {}", consumablesInWarehouseFrom.getQty());
        consumablesInWarehouseFrom.setQty(consumablesInWarehouseFrom.getQty() - interBranchTransfer.qty());
        logger.info("After adjustment ConsumablesInWarehouseFrom.getNumberOfConsumables() = {}", consumablesInWarehouseFrom.getQty());

        consumableInWarehouseRepository.save(consumablesInWarehouseFrom);
        logger.info("Successfully saved the new consumablesInWarehouseFrom");

        ConsumableInterBranchTransfer consumableInterBranchTransfer = ConsumableInterBranchTransfer.builder()
                .consumableInWarehouseTo(consumablesInWarehouseTo)
                .consumableInWarehouseFrom(consumablesInWarehouseFrom)
                .date(interBranchTransfer.date())
                .qty(interBranchTransfer.qty())
                .build();

        return consumableInterBranchTransferRepository.save(consumableInterBranchTransfer);
    }
}
