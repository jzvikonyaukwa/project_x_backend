package com.axe.consumables;

import com.axe.consumables.DTOs.ConsumableDetails;
import com.axe.consumables.DTOs.ConsumableLowStockData;
import com.axe.consumables.DTOs.ConsumablesStockLevelToBeChecked;
import com.axe.consumables.DTOs.InventoryBalanceHistory;
import com.axe.consumablesInWarehouse.ConsumableInWarehouseRepository;
import com.axe.consumablesInWarehouse.ConsumableInWarehouseService;
import com.axe.consumablesInWarehouse.ConsumablesInWarehouse;
import com.axe.consumablesInWarehouse.exceptions.ConsumableInWarehouseNotFoundException;
import com.axe.consumablesOnPurchaseOrder.dtos.ConsumablesOnPurchaseOrder;
import com.axe.consumablesOnQuote.ConsumableOnQuote;
import com.axe.consumablesOnQuote.ConsumablesOnQuoteService;
import com.axe.warehouse.Warehouse;
import com.axe.warehouse.WarehouseService;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class ConsumablesService {

    private final ConsumablesRepository consumablesRepository;
    private final ConsumableInWarehouseService consumableInWarehouseService;
    private final WarehouseService warehouseService;
    private final ConsumablesOnQuoteService consumablesOnQuoteService;
    private final Logger logger = LoggerFactory.getLogger(ConsumablesService.class);
    private final ConsumableInWarehouseRepository consumableInWarehouseRepository;

    public ConsumablesService(ConsumablesRepository consumablesRepository,
            ConsumableInWarehouseService consumableInWarehouseService,
            WarehouseService warehouseService,
            ConsumablesOnQuoteService consumablesOnQuoteService,
            ConsumableInWarehouseRepository consumableInWarehouseRepository) {
        this.consumablesRepository = consumablesRepository;
        this.consumableInWarehouseService = consumableInWarehouseService;
        this.warehouseService = warehouseService;
        this.consumablesOnQuoteService = consumablesOnQuoteService;
        this.consumableInWarehouseRepository = consumableInWarehouseRepository;
    }

    public List<Consumable> getAllConsumables() {
        return consumablesRepository.findAll(
            Sort.by(Sort.Direction.ASC, "name")
        );
    }

    public Consumable getConsumableById(Long id) {
        return consumablesRepository.findById(id).orElse(null);
    }

    @Transactional
    public Consumable saveConsumable(Consumable consumableInfo) {

        Consumable consumable = addConsumable(consumableInfo);
        logger.info("Consumable saved: {}", consumable.toString());
        ConsumablesInWarehouse consumablesInWarehouse = new ConsumablesInWarehouse();
        consumablesInWarehouse.setConsumable(consumable);
        consumablesInWarehouse.setQty(0);
        Warehouse warehouse = warehouseService.getWarehouseByName("owned");
        consumablesInWarehouse.setWarehouse(warehouse);
        consumablesInWarehouse = consumableInWarehouseService.addConsumableInWarehouse(consumablesInWarehouse);
        logger.info("Consumable added to owned warehouse: {}", consumablesInWarehouse.getId());
        return consumable;
    }

    public Consumable addConsumable(Consumable consumable) {
        return consumablesRepository.save(consumable);
    }

    public Consumable updateConsumable(Consumable consumable) {

        Consumable consumableToUpdate = getConsumableById(consumable.getId());
        consumableToUpdate.setName(consumable.getName());
        consumableToUpdate.setSerialNumber(consumable.getSerialNumber());
        consumableToUpdate.setUom(consumable.getUom());
        consumableToUpdate.setSourceCountry(consumable.getSourceCountry());
        consumableToUpdate.setCategory(consumable.getCategory());
        consumableToUpdate.setMinQtyAlertConsignment(consumable.getMinQtyAlertConsignment());
        consumableToUpdate.setMinQtyAlertOwned(consumable.getMinQtyAlertOwned());

        return consumablesRepository.save(consumable);
    }

    public List<ConsumablesOnPurchaseOrder> getConsumablesOnOrder() {
        return consumablesRepository.getConsumablesOnOrder();
    }

    public List<ConsumableLowStockData> getLowStockConsumables() {
        return consumablesRepository.getLowStockConsumables();
    }

    public List<Consumable> getAllConsumableDetails() {
        return consumablesRepository.findAll();
    }

    public List<ConsumablesStockLevelToBeChecked> checkConsumableStockLevels(
            List<ConsumablesStockLevelToBeChecked> consumablesToBeChecked) {
        Warehouse warehouse = warehouseService.getWarehouseByName("owned");

        for (ConsumablesStockLevelToBeChecked consumableToBeChecked : consumablesToBeChecked) {
            ConsumableOnQuote consumableOnQuote = consumablesOnQuoteService
                    .getConsumableOnQuoteById(consumableToBeChecked.getConsumableOnQuoteId());

            if (consumableOnQuote == null) {
                throw new IllegalStateException("Consumable is on a quote and cannot be found");
            }

            ConsumablesInWarehouse consumablesInWarehouse = consumableInWarehouseRepository
                    .findByConsumableIdAndWarehouseId(
                            consumableOnQuote.getConsumable().getId(), warehouse.getId())
                    .orElseThrow(
                            () -> new ConsumableInWarehouseNotFoundException(1011, "Consumable with id [%s] not found"
                                    .formatted(consumableOnQuote.getConsumable().getId())));

            if (consumablesInWarehouse != null) {
                logger.info("Consumable in warehouse qty: {}", consumablesInWarehouse.getQty());
                logger.info("Consumable on quote qty: {}", consumableOnQuote.getQty());
                boolean result = consumablesInWarehouse.getQty() >= consumableOnQuote.getQty();
                logger.info("Stock available: {}", result);
                consumableToBeChecked.setStockAvailable(result);
            }
        }

        return consumablesToBeChecked;
    }

    @Transactional(readOnly = true)
    public List<InventoryBalanceHistory> inventoryBalanceHistory(Long warehouseID, Long consumableID,
            LocalDate startDate) {
        logger.debug("Fetching inventory balance history for consumableID: {} in warehouseID: {} starting from: {}",
                consumableID, warehouseID, startDate);
        try {
            // fetch history starting with current balance thru startDate
            List<InventoryBalanceHistory> balanceHistory = consumablesRepository.historyForConsumable(warehouseID,
                    consumableID, startDate);

            // correct transaction order
            Collections.reverse(balanceHistory);

            return balanceHistory;
        } catch (DataAccessException e) {
            logger.error("Database error fetching consumable history: {}", e.getLocalizedMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unknown error fetching consumable history: {}", e.getLocalizedMessage());
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public ConsumableDetails fetchConsumableDetails(Long consumableID) {
        logger.debug("Fetching consumable details for consumableID: {}", consumableID);
        try {
            Consumable consumable = consumablesRepository.findById(consumableID).orElseThrow( () -> new RuntimeException( "Consumable with id [%s] not found".formatted(consumableID)));
            ConsumableDetails consumableDetails = new ConsumableDetails();
            consumableDetails.setId(consumable.getId());
            consumableDetails.setSerialNumber(consumable.getSerialNumber());
            consumableDetails.setName(consumable.getName());
            consumableDetails.setUom(consumable.getUom());
            consumableDetails.setMinAlertQty(consumable.getMinQtyAlertOwned());
            consumableDetails.setCategory(consumable.getCategory());
            consumableDetails.setSourceCountry(consumable.getSourceCountry());

            return consumableDetails;
        } catch (DataAccessException e) {
            logger.error("Database error fetching consumable details: {}", e.getLocalizedMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unknown error fetching consumable details: {}", e.getLocalizedMessage());
            throw e;
        }
    }
}
