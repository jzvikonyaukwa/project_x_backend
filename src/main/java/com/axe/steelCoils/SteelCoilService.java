package com.axe.steelCoils;

import com.axe.grvs.services.CalculateCostPerMtrService;
import com.axe.saleOrder.models.TotalMtrsRemaingForEachCategoryDTO;
import com.axe.steelCoils.exceptions.SteelCoilNotFoundException;
import com.axe.steelCoils.steelCoilsDTO.SteelCoilDetailsDTO;

import com.axe.steelCoils.steelCoilsDTO.SteelCoilTransactionInformation;
import com.axe.steelSpecifications.SteelSpecification;
import com.axe.stocks.stocksDTO.StockMovementData;
import com.axe.warehouse.Warehouse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class SteelCoilService {
    private static final Logger logger = LoggerFactory.getLogger(SteelCoilService.class);

    private final SteelCoilRepository steelCoilRepository;
    private final CalculateCostPerMtrService calculateCostPerMtrService;

    public SteelCoilService(SteelCoilRepository steelCoilRepository, CalculateCostPerMtrService calculateCostPerMtrService) {
        this.steelCoilRepository = steelCoilRepository;
        this.calculateCostPerMtrService = calculateCostPerMtrService;

    }

    public List<SteelCoil> getAllSteelCoils() {
        logger.info("Getting all steel coils");
        return steelCoilRepository.findAll();
    }

    public SteelCoil getSteelCoil(Long steelCoilId) {
        return steelCoilRepository.findById(steelCoilId)
                .orElseThrow(()-> new SteelCoilNotFoundException("Steel Coil with id [%s] not found".formatted(steelCoilId)));
    }

    public SteelCoil save(SteelCoil steelCoil) {
        return steelCoilRepository.save(steelCoil);
    }

    public SteelCoil getByCoilId(Long coilId) {
        return steelCoilRepository.findById(coilId).orElse(null);
    }

    public SteelCoil createNewSteelCoil(String coilNumber, String cardNumber, BigDecimal weightInKgsOnArrival,
                                        BigDecimal estMeterRunOnArrival, String status,
                                        SteelSpecification steelSpecification, Warehouse warehouse,
                                        BigDecimal conversionRate, BigDecimal landedCostPerKg){

        logger.info("landedCostPerKg: {}", landedCostPerKg);
        BigDecimal costPerMtr = calculateCostPerMtrService.calculateCostPerMeter(weightInKgsOnArrival, estMeterRunOnArrival, landedCostPerKg);

        SteelCoil steelCoil = new SteelCoil();
        steelCoil.setCoilNumber(coilNumber);
        steelCoil.setCardNumber(cardNumber);
        steelCoil.setEstimatedMetersRemaining(estMeterRunOnArrival);
        steelCoil.setEstimatedMeterRunOnArrival(estMeterRunOnArrival);
        steelCoil.setWeightInKgsOnArrival(weightInKgsOnArrival);
        steelCoil.setLandedCostPerMtr(costPerMtr);
        steelCoil.setLandedCostPerKg(landedCostPerKg);
        steelCoil.setConversionRatio(conversionRate);
        steelCoil.setStatus(status);
        steelCoil.setSteelSpecification(steelSpecification);
        steelCoil.setWarehouse(warehouse);
        return steelCoil;
    }



    public boolean isThereEnoughStock(Long warehouseId, String color, BigDecimal gauge, BigDecimal requiredMtrs, BigDecimal width) {
        logger.debug("Checking if there is enough stock for warehouseId: {}, color: {}, gauge: {}, requiredMtrs: {}, width: {}", warehouseId, color, gauge, requiredMtrs, width);
        return steelCoilRepository.isThereEnoughStock(warehouseId, color, gauge, width).compareTo(requiredMtrs) >= 0;
    }

    public BigDecimal getTotalMtrsForAcceptedCuttingLists(BigDecimal gauge, String color, BigDecimal width) {
        logger.debug("Getting total meters for accepted cutting lists for gauge: {}, color: {}, width: {}", gauge, color, width);
        return steelCoilRepository.getTotalMtrsForAcceptedCuttingLists(gauge, color, width);
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateWeightedAvgCostForSteelType(Long colorId, Long gaugeId, Long widthId) {
        logger.debug("Calc weighted average cost for steel type with colorId: {}, gaugeId: {}, widthId: {}", colorId, gaugeId, widthId);

        BigDecimal weightedAverageCostPerMeter = steelCoilRepository.calculateWeightedAvgCostForSteelType(colorId, gaugeId, widthId);
        logger.debug("weightedAverageCostPerMeter: {}", weightedAverageCostPerMeter);

        logger.info("weightedAverageCostPerMeter: {}", weightedAverageCostPerMeter);

        if(weightedAverageCostPerMeter == null || weightedAverageCostPerMeter.compareTo(BigDecimal.ZERO) == 0){
            logger.info("Weighted average cost is zero; fetching landed cost per meter.");

            // Fetch a default landed cost per meter from the steel coils repository
            Optional<BigDecimal> defaultLandedCostPerMtr = steelCoilRepository.findLatestLandedCostPerMtr(colorId, gaugeId, widthId);

            if (defaultLandedCostPerMtr.isPresent()) {
                weightedAverageCostPerMeter = defaultLandedCostPerMtr.get();
                logger.info("Fetched landed cost per meter: {}", weightedAverageCostPerMeter);
            } else {
                logger.warn("No landed cost per meter found. Defaulting to zero.");
                weightedAverageCostPerMeter = BigDecimal.ZERO;
            }
        }

        return weightedAverageCostPerMeter;
    }

    public List<SteelCoilDetailsDTO> getAvailableSteelCoilsForMachine(BigDecimal width) {
        logger.debug("Getting available steel coils for machine with width: {}", width);
        return steelCoilRepository
                .getAvailableSteelCoilsForMachine(width)
                .stream()
                .filter(steelCoilDetailsDTO -> steelCoilDetailsDTO
                        .getEstMtrsRemaining()
                        .compareTo(BigDecimal.ZERO) > 0)
                .toList();
    }


    public SteelCoilDetailsDTO getSteelCoilDetails(Long steelCoilId) {
        return steelCoilRepository.getSteelCoilDetails(steelCoilId);
    }

    public List<SteelCoilTransactionInformation> getSteelCoilTransactions(Long steelCoilId) {
        return steelCoilRepository.getSteelCoilTransactions(steelCoilId);
    }

    public BigDecimal getStockLevelsForSteelType(BigDecimal width, BigDecimal gauge, String color) {
        return steelCoilRepository.getStockLevelsForSteelType(width, gauge, color);
    }
    public List<TotalMtrsRemaingForEachCategoryDTO> getAllStockLevels() {
        return steelCoilRepository.getAllStockLevels();
    }

    public List<StockMovementData> getStockMovement() {
        return steelCoilRepository.getStockMovement();
    }

    public Optional<SteelCoil> getSteelCoilByCoilNumberInWarehouse(Long id, String steelCoilNumber) {
        return steelCoilRepository.getSteelCoilByCoilNumberInWarehouse(id, steelCoilNumber);
    }

    public List<SteelCoilDetailsDTO> getFilteredAvailableSteelCoils(BigDecimal width, Double gauge, String color) {
        logger.debug("Getting filtered available steel coils for width: {}, gauge: {}, color: {}", width, gauge, color);


        // Retrieve steel coils based on the provided parameters
        List<SteelCoilDetailsDTO> steelCoilDetailsDTOS = steelCoilRepository.getFilteredAvailableSteelCoils(width, gauge, color);

        // Additional retrieval of 100mm if width equals 182
        if (width.equals(BigDecimal.valueOf(182))) {
            steelCoilDetailsDTOS.addAll(steelCoilRepository.getFilteredAvailableSteelCoils(BigDecimal.valueOf(100), gauge, color));
        }

        // Filter the steel coils based on remaining meters and return the result
        return steelCoilDetailsDTOS.stream()
                .filter(steelCoilDetailsDTO -> steelCoilDetailsDTO.getEstMtrsRemaining().compareTo(BigDecimal.ZERO) > 0)
                .toList();
    }


    public boolean searchForSteelCoilUsingGaugeColorAndCoating(BigDecimal gauge,String color, String coating) {
        logger.debug("Searching for steel coil using gauge: {}, color: {}, coating: {}", gauge, color, coating);
        return steelCoilRepository.searchForSteelCoilUsingGaugeColorAndCoating(gauge, color, coating)>0;
    }
}
