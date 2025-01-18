package com.axe.weightConversionsServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class WeightConversionService {

    private final GaugeRateMapper gaugeRateMapper;
    private final Logger logger = LoggerFactory.getLogger(WeightConversionService.class);

    public WeightConversionService(GaugeRateMapper gaugeRateMapper) {
        this.gaugeRateMapper = gaugeRateMapper;
    }

    public BigDecimal calculateConversionRate(BigDecimal width, BigDecimal gauge) {
        if ((width.compareTo(new BigDecimal("925"))) == 0) {
            System.out.println("Width is 0.925. Looking up rate for " + gauge + "...");
            logger.info("Width is 0.925. Looking up rate for {}...", gauge);
            return gaugeRateMapper.getRate(gauge);
        } else {
            BigDecimal adjustedWidth = width.divide(BigDecimal.valueOf(1000), 4, RoundingMode.HALF_UP);
            BigDecimal conversionRate = adjustedWidth
                    .multiply(gauge)
                    .multiply(BigDecimal.valueOf(1))
                    .multiply(new BigDecimal("8.039"));

            logger.info("Width is not 925. Calculating conversion rate...");
            logger.info("Width: {}", adjustedWidth);
            logger.info("Gauge: {}", gauge);

            logger.info("Conversion rate: {}", conversionRate);
            return conversionRate;
        }
    }

}
