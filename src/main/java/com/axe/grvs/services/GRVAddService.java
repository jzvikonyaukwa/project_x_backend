package com.axe.grvs.services;

import com.axe.consumables.CalculateWeightedAvgPriceService;
import com.axe.consumables.Consumable;
import com.axe.consumables.ConsumablesService;
import com.axe.consumables.DTOs.ConsumablePostDTO;
import com.axe.consumablesInWarehouse.ConsumableInWarehouseRepository;
import com.axe.consumablesInWarehouse.ConsumableInWarehouseService;
import com.axe.consumablesInWarehouse.ConsumablesInWarehouse;
import com.axe.consumablesInWarehouse.exceptions.ConsumableInWarehouseNotFoundException;
import com.axe.consumablesOnGrv.ConsumablesOnGrv;
import com.axe.consumablesOnGrv.ConsumablesOnGrvService;
import com.axe.grvs.GRV;
import com.axe.grvs.GRVsRepository;
import com.axe.grvs.exceptions.NoProductsFoundException;
import com.axe.grvs.grvsDTO.GRVDetailsDTO;
import com.axe.grvs.grvsDTO.SteelCoilPostDTO;
import com.axe.purchaseOrders.PurchaseOrder;

import com.axe.purchaseOrders.services.PurchaseOrderService;
import com.axe.steelCoils.SteelCoil;
import com.axe.steelCoils.SteelCoilService;
import com.axe.steelSpecifications.SteelSpecification;
import com.axe.steelSpecifications.SteelSpecificationService;
import com.axe.suppliers.Supplier;
import com.axe.suppliers.SuppliersService;
import com.axe.utilities.RateNotSavedException;
import com.axe.utilities.SteelCoilNumberExists;
import com.axe.warehouse.Warehouse;
import com.axe.weightConversionsServices.WeightConversionService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class GRVAddService {
    private static final Logger logger = LoggerFactory.getLogger(GRVAddService.class);

    private final SuppliersService suppliersService;
    private final SteelSpecificationService steelSpecificationService;
    private final WeightConversionService weightConversionService;
    private final SteelCoilService steelCoilService;
    private final ConsumablesService consumablesService;
    private final ConsumableInWarehouseService consumableInWarehouseService;
    private final ConsumablesOnGrvService consumablesOnGrvService;
    private final GRVsRepository grvsRepository;
    private final CalculateWeightedAvgPriceService calculateWeightedAvgPriceService;
    private final ConsumableInWarehouseRepository consumableInWarehouseRepository;
    private final PurchaseOrderService purchaseOrderService;

    public GRVAddService(SuppliersService suppliersService,
            SteelSpecificationService steelSpecificationService,
            WeightConversionService weightConversionService,
            SteelCoilService steelCoilService, ConsumablesService consumablesService,
            ConsumableInWarehouseService consumableInWarehouseService,
            ConsumablesOnGrvService consumablesOnGrvService, GRVsRepository grvsRepository,
            CalculateWeightedAvgPriceService calculateWeightedAvgPriceService,
            ConsumableInWarehouseRepository consumableInWarehouseRepository,
            PurchaseOrderService purchaseOrderService) {

        this.suppliersService = suppliersService;
        this.steelSpecificationService = steelSpecificationService;
        this.weightConversionService = weightConversionService;
        this.steelCoilService = steelCoilService;
        this.consumablesService = consumablesService;
        this.consumableInWarehouseService = consumableInWarehouseService;
        this.consumablesOnGrvService = consumablesOnGrvService;
        this.grvsRepository = grvsRepository;
        this.purchaseOrderService = purchaseOrderService;
        this.calculateWeightedAvgPriceService = calculateWeightedAvgPriceService;
        this.consumableInWarehouseRepository = consumableInWarehouseRepository;
    }

    @Transactional
    public GRVDetailsDTO createGRV(GRVDetailsDTO postGRV) {
        try {
            logger.debug("Creating a new GRV...: {}", postGRV);
            runCoilNumberDatabaseChecks(postGRV.getSteelCoils(), postGRV.getWarehouse());
            GRV newGRV = new GRV();

            if (postGRV.getSteelCoils().isEmpty() && postGRV.getConsumablesOnGrv().isEmpty()) {
                logger.info("No products to add. Returning...");
                throw new NoProductsFoundException(409, "No products to add");
            }

            logger.info("Finished checking coil numbers. Proceeding to create GRV...");

            newGRV.setDateReceived(postGRV.getDateReceived());
            newGRV.setComments(postGRV.getComments());
            newGRV.setSupplierGRVCode(postGRV.getSupplierGrvCode());
            handlePurchaseOrder(postGRV, newGRV);
            newGRV = grvsRepository.save(newGRV);

            logger.info("Added the GRV. newGRV.id = {}", newGRV.getId());

            Supplier supplier = suppliersService.getSupplier(postGRV.getSupplierId());
            logger.info("Supplier: {}", supplier.getName());

            if (!postGRV.getSteelCoils().isEmpty()) {
                handleGRVsProducts(postGRV.getSteelCoils(), newGRV, postGRV.getWarehouse(), supplier);
            }

            if (!postGRV.getConsumablesOnGrv().isEmpty()) {
                logger.info("Consumables are not empty. Handling consumables...");
                try {
                    handleGRVsConsumables(postGRV.getConsumablesOnGrv(), postGRV.getWarehouse(), newGRV);
                } catch (Exception e) {
                    logger.info("Error handling consumables: {}", e.getMessage());
                }
            } else {
                logger.info("Consumables are empty. Returning...");
            }
            logger.info("Returning the new GRV: {}", newGRV);
            postGRV.setId(newGRV.getId());
            grvsRepository.save(newGRV);
            logger.info("GRV saved successfully.");

            return postGRV;
        } catch (DataAccessException e) {
            logger.error("Error creating grv: {}", e.getMostSpecificCause().getLocalizedMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error creating grv: {}", e.getLocalizedMessage());
            throw e;
        }

    }

    public void handlePurchaseOrder(GRVDetailsDTO postGRV, GRV newGRV) {
        PurchaseOrder purchaseOrder = purchaseOrderService.getPurchaseOrder(postGRV.getPurchaseOrderId());
        if (purchaseOrder == null) {
            throw new RuntimeException("No purchase with that ID");
        }
        newGRV.setPurchaseOrder(purchaseOrder);
    }

    public void runCoilNumberDatabaseChecks(List<SteelCoilPostDTO> products, Warehouse warehouse)
            throws SteelCoilNumberExists {
        for (SteelCoilPostDTO steelCoilsDTO : products) {

            if (coilNumberExistsInWarehouse(warehouse, steelCoilsDTO.getCoilNumber())) {
                logger.error("Found a steel coil with the same coil number in the same warehouse. Coil number: {}",
                        steelCoilsDTO.getCoilNumber());

                throw new SteelCoilNumberExists(409,
                        "Found a steel coil with the same coil number in the same warehouse. " +
                                "Coil number: " + steelCoilsDTO.getCoilNumber());
            } else {
                logger.info("No steel coil with that coil number in that warehouse. Coil number: {}",
                        steelCoilsDTO.getCoilNumber());
            }
        }
    }

    @Transactional
    public void handleGRVsProducts(List<SteelCoilPostDTO> products, GRV newGRV, Warehouse warehouse, Supplier supplier) {

        logger.info("Handling steel coils...");

        for (SteelCoilPostDTO steelCoilsDTO : products) {
            SteelSpecification steelSpecification = steelSpecificationService.handleSteelSpecification(steelCoilsDTO);
            if (steelSpecification == null || steelSpecification.getWidth() == null) {
                logger.error("Steel specification or width is null for coil number: {}", steelCoilsDTO.getCoilNumber());
                throw new IllegalStateException("Steel specification or width is missing.");
            }

            BigDecimal width = steelSpecification.getWidth().getWidth();
            BigDecimal gauge = steelCoilsDTO.getGauge().getGauge();
            BigDecimal conversionRate = weightConversionService.calculateConversionRate(width, gauge);

            logger.info("Conversion rate: {}", conversionRate);
            if (conversionRate == null || conversionRate.compareTo(BigDecimal.ZERO) == 0) {
                logger.info("Conversion rate is zero or less. Looks like no constant has been set.");
                throw new RateNotSavedException("Conversion is not saved in the system.");
            }

            BigDecimal estMeterRunOnArrival = steelCoilsDTO.getWeightInKgsOnArrival()
                    .divide(conversionRate, 4, RoundingMode.HALF_UP);

            logger.info("Estimated meter run on arrival: {}", estMeterRunOnArrival);

            String newCardNumber;

            if (warehouse.getName().equals("owned")) {
                newCardNumber = steelCoilsDTO.getCardNumber() + "-O";
            } else {
                newCardNumber = steelCoilsDTO.getCardNumber() + "-C";
            }

            logger.info("Product on purchase order landed Cost per kg {}", steelCoilsDTO.getLandedCostPerKg());
            logger.info("Product on purchase order Purchase cost per kg {}", steelCoilsDTO.getPurchaseCostPerKg());
            SteelCoil newSteelCoil = steelCoilService.createNewSteelCoil(
                    steelCoilsDTO.getCoilNumber(), newCardNumber,
                    steelCoilsDTO.getWeightInKgsOnArrival(), estMeterRunOnArrival, "in-stock",
                    steelSpecification, warehouse, conversionRate, steelCoilsDTO.getLandedCostPerKg());

            newSteelCoil.setConsignor(steelCoilsDTO.getConsignor());
            newSteelCoil.setSupplier(supplier);
            newSteelCoil.setGrv(newGRV);
            SteelCoil saveSteelCoil = steelCoilService.save(newSteelCoil);
            newGRV.getSteelCoils().add(saveSteelCoil);

        }
    }

    boolean coilNumberExistsInWarehouse(Warehouse warehouse, String steelCoilNumber) {

        return steelCoilService
                .getSteelCoilByCoilNumberInWarehouse(warehouse.getId(), steelCoilNumber)
                .isPresent();
    }

    public void handleGRVsConsumables(List<ConsumablePostDTO> consumables, Warehouse warehouse, GRV newGRV) {
        logger.info("Handling consumables...");
        if (warehouse == null) {
            throw new ConsumableInWarehouseNotFoundException(404, "Warehouse not found");
        }
        if (newGRV == null) {
            throw new ConsumableInWarehouseNotFoundException(404, "GRV not found");
        }

        for (ConsumablePostDTO consumablePostDTO : consumables) {
            Consumable consumable = consumablesService.getConsumableById(consumablePostDTO.getConsumable().getId());

            logger.info("Consumable name: {}", consumable.getName());
            ConsumablesInWarehouse stockItem = consumableInWarehouseRepository
                    .findByConsumableIdAndWarehouseId(
                            consumablePostDTO.getConsumable().getId(), warehouse.getId())
                    .orElse(null);
            if (stockItem == null) {
                logger.info("ConsumablesInWarehouse is null. Creating a new one...");
                stockItem = new ConsumablesInWarehouse();
                stockItem.setWarehouse(warehouse);
                stockItem.setAvgLandedPrice(consumablePostDTO.getLandedPrice());
                stockItem.setQty(consumablePostDTO.getQtyOrdered());
            } else {
                logger.info("ConsumablesInWarehouse: {}", stockItem.getConsumable().getName());
                logger.info("ConsumablesInWarehouse is not null. Updating the qty and avgLandedPrice...");
                logger.info("Stock qty: {}", stockItem.getQty());
                logger.info("Grv qty: {}", consumablePostDTO.getQtyOrdered());
                logger.info("Current avgLandedPrice: {}", stockItem.getAvgLandedPrice());
                BigDecimal newAvgLandedPrice = calculateWeightedAvgPriceService.calculateNewWeightedAverage(
                        stockItem.getAvgLandedPrice(),
                        new BigDecimal(stockItem.getQty()),
                        consumablePostDTO.getLandedPrice(),
                        new BigDecimal(consumablePostDTO.getQtyOrdered()));
                logger.info("New avgLandedPrice: {}", newAvgLandedPrice);
                stockItem.setAvgLandedPrice(newAvgLandedPrice);
                stockItem.setQty(stockItem.getQty() + consumablePostDTO.getQtyOrdered()); // TODO: should only set after
                                                                                          // calc weighted avg price
            }

            stockItem.setConsumable(consumable);
            stockItem = consumableInWarehouseService.saveConsumableInWarehouse(stockItem);

            ConsumablesOnGrv receivedItem = new ConsumablesOnGrv();
            receivedItem.setQtyReceived(consumablePostDTO.getQtyOrdered());
            receivedItem.setLandedPrice(consumablePostDTO.getLandedPrice());
            receivedItem.setConsumableInWarehouse(stockItem);
            receivedItem.setGrv(newGRV);

            consumablesOnGrvService.saveConsumablesOnGrv(receivedItem);

            logger.info("Just saved the consumableInWarehouse: {}", stockItem);
            logger.info("ConsumablesInWarehouse updated successfully.");

        }
    }
}
