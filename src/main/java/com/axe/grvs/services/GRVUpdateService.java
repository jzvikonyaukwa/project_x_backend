package com.axe.grvs.services;

import com.axe.consumables.CalculateWeightedAvgPriceService;
import com.axe.consumablesInWarehouse.ConsumableInWarehouseRepository;
import com.axe.consumablesInWarehouse.ConsumablesInWarehouse;
import com.axe.consumablesInWarehouse.exceptions.ConsumableInWarehouseNotFoundException;
import com.axe.consumablesOnGrv.ConsumablesOnGrv;
import com.axe.consumablesOnGrv.ConsumablesOnGrvRepository;
import com.axe.consumablesOnGrv.ConsumablesOnGrvService;
import com.axe.consumablesOnGrv.exceptions.ConsumableOnGrvNotFoundException;
import com.axe.grvs.GRV;
import com.axe.grvs.GRVsRepository;
import com.axe.grvs.grvsDTO.GRVDetailsDTO;
import com.axe.grvs.grvsDTO.SteelCoilPostDTO;
import com.axe.steelCoils.SteelCoil;
import com.axe.steelCoils.SteelCoilService;
import com.axe.steelSpecifications.SteelSpecification;
import com.axe.steelSpecifications.SteelSpecificationService;
import com.axe.weightConversionsServices.WeightConversionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class GRVUpdateService {
    private final GRVsService grvsService;
    private final Logger logger = LoggerFactory.getLogger(GRVUpdateService.class);
    private final SteelCoilService steelCoilService;
    private final SteelSpecificationService steelSpecificationService;
    private final ConsumablesOnGrvService consumablesOnGrvService;
    private final ConsumableInWarehouseRepository consumableInWarehouseRepository;
    private final CalculateWeightedAvgPriceService calculateWeightedAvgPriceService;
    private final GRVsRepository grvsRepository;
    private final WeightConversionService weightConversionService;
    private final ConsumablesOnGrvRepository consumablesOnGrvRepository;

    public GRVUpdateService(GRVsService grvsService,
                            SteelCoilService steelCoilService,
                            SteelSpecificationService steelSpecificationService,
                            ConsumablesOnGrvService consumablesOnGrvService,
                            ConsumableInWarehouseRepository consumableInWarehouseRepository,
                            CalculateWeightedAvgPriceService calculateWeightedAvgPriceService,
                            GRVsRepository grvsRepository,
                            WeightConversionService weightConversionService,
                            ConsumablesOnGrvRepository consumablesOnGrvRepository
                            ) {

        this.grvsService = grvsService;
        this.steelCoilService = steelCoilService;
        this.steelSpecificationService = steelSpecificationService;
        this.consumablesOnGrvService = consumablesOnGrvService;
        this.consumableInWarehouseRepository = consumableInWarehouseRepository;
        this.calculateWeightedAvgPriceService = calculateWeightedAvgPriceService;
        this.grvsRepository = grvsRepository;
        this.weightConversionService = weightConversionService;
        this.consumablesOnGrvRepository = consumablesOnGrvRepository;
    }

    public GRVDetailsDTO updateGRV(GRVDetailsDTO grv) {

        GRV grvToUpdate = createGRVToBeUpdated(grv);
        handleConsumables(grv);
        handleSteelCoils(grv);
        grvsRepository.save(grvToUpdate);
        return grv;
    }

    private GRV createGRVToBeUpdated(GRVDetailsDTO grv) {
        logger.info("GRV ID: {}", grv.getId());
        GRV grvToUpdate = grvsService.getGRV(grv.getId());
        grvToUpdate.setDateReceived(grv.getDateReceived());
        grvToUpdate.setComments(grv.getComments());
        grvToUpdate.setSupplierGRVCode(grv.getSupplierGrvCode());
        return grvToUpdate;
    }

    private void handleConsumables(GRVDetailsDTO grv) {
        grv.getConsumablesOnGrv().forEach(consumablePostDTO -> {
            ConsumablesOnGrv consumablesOnGrv
                    = consumablesOnGrvRepository.findById(consumablePostDTO.getConsumableOnGrvId())
                    .orElseThrow(() ->
                            new ConsumableOnGrvNotFoundException(1011,"Consumable with id [%s] not found"
                                    .formatted(consumablePostDTO.getConsumableOnGrvId())))
                    ;

            BigDecimal originalLandedPrice = consumablesOnGrv.getLandedPrice();
            BigDecimal newLandedPrice = consumablePostDTO.getLandedPrice();
            BigDecimal landedPriceDifference = newLandedPrice.subtract(originalLandedPrice);

            Integer originalQtyReceived = consumablesOnGrv.getQtyReceived();
            Integer newQtyReceived = consumablePostDTO.getQtyOrdered();

            consumablesOnGrv.setQtyReceived(consumablePostDTO.getQtyOrdered());
            consumablesOnGrv.setLandedPrice(consumablePostDTO.getLandedPrice());

            ConsumablesInWarehouse consumableInWarehouse = consumableInWarehouseRepository
                    .findById(consumablePostDTO.getConsumableInWarehouseId())
                    .orElseThrow(() ->
                            new ConsumableInWarehouseNotFoundException(1011,"could not find consumable with id [%s] in warehouse"
                                    .formatted(consumablePostDTO.getConsumableInWarehouseId())));

            BigDecimal newAvgLandedPrice = calculateWeightedAvgPriceService.calculateNewWeightedAverage(
                    consumableInWarehouse.getAvgLandedPrice(),
                    new BigDecimal(consumableInWarehouse.getQty()),
                    consumablePostDTO.getLandedPrice(),
                    new BigDecimal(consumablePostDTO.getQtyOrdered())
            );

            consumableInWarehouse.setAvgLandedPrice(newAvgLandedPrice);
            consumableInWarehouse.setQty(consumableInWarehouse.getQty() + newQtyReceived - originalQtyReceived);
            consumableInWarehouse.setAvgLandedPrice(consumableInWarehouse.getAvgLandedPrice().add(landedPriceDifference));

            consumableInWarehouseRepository.save(consumableInWarehouse);
            consumablesOnGrvService.saveConsumablesOnGrv(consumablesOnGrv);
        });
    }

    private void handleSteelCoils(GRVDetailsDTO grv) {
        grv.getSteelCoils().forEach(steelCoilPostDTO -> {
            SteelCoil steelCoil = steelCoilService.getSteelCoil(steelCoilPostDTO.getSteelCoilId());
            steelCoil.setCoilNumber(steelCoilPostDTO.getCoilNumber());
            steelCoil.setCardNumber(steelCoilPostDTO.getCardNumber());
            steelCoil.setStatus("in-stock");

            handleSteelCoilWeightChange(steelCoil, steelCoilPostDTO);

            steelCoil.setLandedCostPerMtr(steelCoilPostDTO.getLandedCostPerMtr());
            steelCoil.setWarehouse(grv.getWarehouse());
            logger.info("Warehouse: {}", steelCoil.getWarehouse().getName());

            SteelSpecification steelSpecification = steelSpecificationService.findSteelSpecification(
                    steelCoilPostDTO.getColor().getId(), steelCoilPostDTO.getIsqGrade(), steelCoilPostDTO.getWidth().getId(),
                    steelCoilPostDTO.getCoating(), steelCoilPostDTO.getGauge().getId()
            ).orElseGet(() -> {
                        logger.info("Steel Specification NOT found! Creating a new one...");

                        SteelSpecification steelSpecificationBuild
                                =SteelSpecification
                                .builder()
                                .color(steelCoilPostDTO.getColor())
                                .ISQGrade(steelCoilPostDTO.getIsqGrade())
                                .width(steelCoilPostDTO.getWidth())
                                .coating(steelCoilPostDTO.getCoating())
                                .gauge(steelCoilPostDTO.getGauge())
                                .build();

                        return steelSpecificationService.saveNewProductType(steelSpecificationBuild);
                    }

                    );

            steelCoil.setSteelSpecification(steelSpecification);
            logger.info("steelCoil warehouse: {}", steelCoil.getWarehouse().getName());
            steelCoilService.save(steelCoil);
        });

    }

    private void handleSteelCoilWeightChange(SteelCoil steelCoil, SteelCoilPostDTO newCoil) {




        if(!steelCoil.getEstimatedMetersRemaining().equals(steelCoil.getEstimatedMeterRunOnArrival())){
            logger.info("Estimated meters remaining is not equal to estimated meter run on arrival. " +
                    "This means the coil has been used and weights cannot be changed");
            return;
        }

        logger.info("Weight in Kgs on arrival: {}", steelCoil.getWeightInKgsOnArrival());
        logger.info("New weight in Kgs on arrival: {}", newCoil.getWeightInKgsOnArrival());

        if(steelCoil.getWeightInKgsOnArrival().equals(newCoil.getWeightInKgsOnArrival())) {
            logger.info("No change in weight");
        } else {
            logger.info("Change in weight");
            steelCoil.setWeightInKgsOnArrival(newCoil.getWeightInKgsOnArrival());
            BigDecimal width = newCoil.getWidth().getWidth();
            BigDecimal gauge = newCoil.getGauge().getGauge();

            BigDecimal conversionRate = weightConversionService.calculateConversionRate(width, gauge);

            if (conversionRate.compareTo(BigDecimal.ZERO) == 0) {
                logger.info("Conversion rate is zero or less. Looks like no constant has been set.");
            }

            BigDecimal estMeterRunOnArrival = steelCoil.getWeightInKgsOnArrival()
                    .divide(conversionRate, 4, RoundingMode.HALF_UP);

            steelCoil.setEstimatedMeterRunOnArrival(estMeterRunOnArrival);
            steelCoil.setEstimatedMetersRemaining(estMeterRunOnArrival);
        }
    }
}
