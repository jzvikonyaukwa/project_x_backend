package com.axe.steelSpecifications;

import com.axe.colors.Color;
import com.axe.finishes.Finish;
import com.axe.gauges.Gauge;
import com.axe.grvs.grvsDTO.SteelCoilPostDTO;
import com.axe.stocks.stocksDTO.StocksDTO;
import com.axe.width.Width;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class SteelSpecificationService {

    private final SteelSpecificationRepository steelSpecificationRepository;
    Logger logger = LoggerFactory.getLogger(SteelSpecificationService.class);

    public SteelSpecificationService(SteelSpecificationRepository steelSpecificationRepository) {
        this.steelSpecificationRepository = steelSpecificationRepository;
    }

    public List<SteelSpecification> getAllProductTypes() {
        return steelSpecificationRepository.findAll();
    }

    public List<StocksDTO> getStockOnHand(){
        return steelSpecificationRepository.getStockOnHand();
    }

    public Optional<SteelSpecification> findSteelSpecification(Long colorId, String ISQGrade, Long widthId,
                                                              String coating, Long gaugeId){
        logger.info("widthId {}", widthId);
        return steelSpecificationRepository.findSteelSpecification(colorId, ISQGrade, widthId, coating, gaugeId);
    }

    public SteelSpecification saveNewProductType(SteelSpecification productType) {
        return steelSpecificationRepository.save(productType);
    }

    public SteelSpecification createNewSteelSpecification(Finish finish, Color color, String ISQGrade, Width width,
                                                          String coating, Gauge gauge) {
        SteelSpecification steelSpecification = new SteelSpecification();
        steelSpecification.setColor(color);
        steelSpecification.setISQGrade(ISQGrade);
        steelSpecification.setWidth(width);
        steelSpecification.setCoating(coating);
        steelSpecification.setGauge(gauge);
        return steelSpecification;

    }

    public SteelSpecification handleSteelSpecification(SteelCoilPostDTO steelCoilsDTO) {

        logger.info("ISQGrade {}", steelCoilsDTO.getIsqGrade());
        logger.info("width {}", steelCoilsDTO.getWidth().getWidth());
        logger.info("coating {}", steelCoilsDTO.getCoating());
        logger.info("gauge {}", steelCoilsDTO.getGauge().getGauge());
        logger.info("color {}", steelCoilsDTO.getColor().getColor());
        logger.info("ISQGrade {}", steelCoilsDTO.getIsqGrade());

        return findSteelSpecification(
                steelCoilsDTO.getColor().getId(), steelCoilsDTO.getIsqGrade(), steelCoilsDTO.getWidth().getId(),
                steelCoilsDTO.getCoating(), steelCoilsDTO.getGauge().getId())
                .orElseGet(() -> {
                    logger.info("Steel Specification NOT found! Creating a new one...");

                    SteelSpecification steelSpecificationBuild = SteelSpecification.builder()
                            .color(steelCoilsDTO.getColor())
                            .ISQGrade(steelCoilsDTO.getIsqGrade())
                            .width(steelCoilsDTO.getWidth())
                            .coating(steelCoilsDTO.getCoating())
                            .gauge(steelCoilsDTO.getGauge())
                            .productsOnPurchaseOrders(new HashSet<>())  // Initialize the Set
                            .build();
                    return saveNewProductType(steelSpecificationBuild);
                });
    }

}
