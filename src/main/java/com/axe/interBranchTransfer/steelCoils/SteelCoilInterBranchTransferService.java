package com.axe.interBranchTransfer.steelCoils;

import com.axe.interBranchTransfer.DTOs.ConsumableInterBranchTransferDetails;
import com.axe.interBranchTransfer.DTOs.InterBranchTransferDetails;
import com.axe.interBranchTransfer.DTOs.SteelCoilInterBranchTransferDetails;
import com.axe.interBranchTransfer.DTOs.SteelCoilInterBranchTransferDTO;
import com.axe.steelCoils.CardNumberService;
import com.axe.steelCoils.SteelCoil;
import com.axe.steelCoils.SteelCoilRepository;
import com.axe.steelSpecifications.SteelSpecification;
import com.axe.warehouse.Warehouse;
import com.axe.warehouse.WarehouseRepository;
import com.axe.weightConversionsServices.WeightConversionService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class SteelCoilInterBranchTransferService {
    private final SteelCoilInterBranchTransferRepository steelCoilInterBranchTransferRepository;
    private final WarehouseRepository warehouseRepository;
    private final WeightConversionService weightConversionService;
    private final CardNumberService cardNumberService;
    private final SteelCoilRepository steelCoilRepository;
    private final Logger logger = LoggerFactory.getLogger(SteelCoilInterBranchTransferService.class);
    public SteelCoilInterBranchTransferService(SteelCoilInterBranchTransferRepository steelCoilInterBranchTransferRepository,
                                               WeightConversionService weightConversionService,
                                               CardNumberService cardNumberService,
                                               SteelCoilRepository steelCoilRepository,
                                               WarehouseRepository warehouseRepository
    ) {
        this.steelCoilInterBranchTransferRepository = steelCoilInterBranchTransferRepository;
        this.weightConversionService = weightConversionService;
        this.cardNumberService = cardNumberService;
        this.steelCoilRepository = steelCoilRepository;
        this.warehouseRepository= warehouseRepository;
    }

    public List<SteelCoilInterBranchTransfer> getAllInterBranchTransfers() {
        return steelCoilInterBranchTransferRepository.findAll();
    }

    @Transactional
    public SteelCoilInterBranchTransfer createInterBranchTransfer(SteelCoilInterBranchTransferDTO transferPostDTO) {

        SteelCoil steelCoilFrom = steelCoilRepository
                .findById(transferPostDTO.steelCoilIdFrom())
                .orElseThrow(()->
                        new RuntimeException("Steel coil with id [%s] not found"
                                .formatted(transferPostDTO.steelCoilIdFrom())));



        Warehouse warehouse = warehouseRepository.getWarehouseByName("owned");

        logger.info("warehouse: {} id: {}", warehouse.getName(), warehouse.getId());
        logger.info("steelCoilFrom: {}", steelCoilFrom.getCoilNumber());

        SteelSpecification steelSpecification = steelCoilFrom.getSteelSpecification();
        BigDecimal width = steelSpecification.getWidth().getWidth();
        BigDecimal gauge = steelSpecification.getGauge().getGauge();
        BigDecimal conversionRate = weightConversionService.calculateConversionRate(width, gauge);
        BigDecimal weightTransferred = transferPostDTO.metres().multiply(conversionRate);

        SteelCoil steelCoilTo
                = steelCoilRepository.getSteelCoilByCoilNumberInWarehouse(warehouse.getId(), steelCoilFrom.getCoilNumber())
                .map(steelCoil -> {

                            logger.info("Steel coil with that coil number in owned warehouse");
                            steelCoil.setWeightInKgsOnArrival(steelCoil.getWeightInKgsOnArrival().add(weightTransferred));
                            steelCoil.setEstimatedMeterRunOnArrival(steelCoil.getEstimatedMeterRunOnArrival().add(transferPostDTO.metres()));
                            steelCoil.setEstimatedMetersRemaining(steelCoil.getEstimatedMetersRemaining().add(transferPostDTO.metres()));
                            steelCoil.setSupplier(steelCoilFrom.getSupplier());
                            steelCoil.setWarehouse(warehouse);

                    return steelCoil;
                }
                )
                .orElseGet(()-> {

                    String newCardNumber = cardNumberService.changeCardNumber(steelCoilFrom.getCardNumber());


                return   SteelCoil.builder()
                        .coilNumber(steelCoilFrom.getCoilNumber())
                        .cardNumber(newCardNumber)
                        .weightInKgsOnArrival(weightTransferred)
                        .estimatedMeterRunOnArrival(transferPostDTO.metres())
                        .landedCostPerMtr(steelCoilFrom.getLandedCostPerMtr())
                        .status(steelCoilFrom.getStatus())
                        .steelSpecification(steelCoilFrom.getSteelSpecification())
                        .warehouse(warehouse)
                        .build();
                });

        steelCoilRepository.save(steelCoilTo);

        steelCoilFrom.setEstimatedMetersRemaining(steelCoilFrom.getEstimatedMetersRemaining().subtract(transferPostDTO.metres()));
        steelCoilRepository.save(steelCoilFrom);

        SteelCoilInterBranchTransfer newSteelCoilInterBranchTransfer =  SteelCoilInterBranchTransfer.builder()
                .mtrs(transferPostDTO.metres())
                .date(transferPostDTO.date())
                .steelCoilTo(steelCoilTo)
                .steelCoilFrom(steelCoilFrom)
                .build();

        logger.info("Saved newSteelCoilInterBranchTransfer: {}", newSteelCoilInterBranchTransfer.getId());
        return steelCoilInterBranchTransferRepository.save(newSteelCoilInterBranchTransfer);
    }


    public List<SteelCoilInterBranchTransferDetails> getAllSteelCoilInterBranchTransferDetails() {
        return steelCoilInterBranchTransferRepository.getAllSteelCoilInterBranchTransferDetails();
    }

    public List<ConsumableInterBranchTransferDetails> getAllConsumableInterBranchTransferDetails() {
        return steelCoilInterBranchTransferRepository.getAllConsumableInterBranchTransferDetails();
    }

    public List<InterBranchTransferDetails> getSteelCoilInterBranchTransferDetailsForSteelCoil(Long id) {
        return steelCoilInterBranchTransferRepository.getSteelCoilInterBranchTransferDetailsForSteelCoil(id);
    }
}
