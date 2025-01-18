package com.axe.weightConversionsServices;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class GaugeRateMapper {

    private final Map<BigDecimal, BigDecimal> gaugeToRateMap = new HashMap<>();

    public GaugeRateMapper() {
        gaugeToRateMap.put(new BigDecimal("0.25"), new BigDecimal("1.836"));
        gaugeToRateMap.put(new BigDecimal("0.27"), new BigDecimal("1.983"));
        gaugeToRateMap.put(new BigDecimal("0.30"), new BigDecimal("2.204"));
        gaugeToRateMap.put(new BigDecimal("0.40"), new BigDecimal("2.974"));
        gaugeToRateMap.put(new BigDecimal("0.47"), new BigDecimal("3.494"));
        gaugeToRateMap.put(new BigDecimal("0.50"), new BigDecimal("3.718"));
        gaugeToRateMap.put(new BigDecimal("0.58"), new BigDecimal("4.312"));
        gaugeToRateMap.put(new BigDecimal("0.80"), new BigDecimal("5.949"));
    }

    public BigDecimal getRate(BigDecimal gauge) {
        for (Map.Entry<BigDecimal, BigDecimal> entry : gaugeToRateMap.entrySet()) {
            BigDecimal gaugeValue = entry.getKey();
            BigDecimal rateValue = entry.getValue();

            if (gauge.subtract(gaugeValue).abs().compareTo(new BigDecimal("0.0001")) < 0) {
                return rateValue;
            }
        }
        return new BigDecimal("-0.0");
    }
}
