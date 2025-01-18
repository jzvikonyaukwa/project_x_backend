package com.axe.grvs.services;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CalculateCostPerMtrService {

    public BigDecimal calculateCostPerMeter(BigDecimal weightKg,
                                            BigDecimal totalMtrs,
                                            BigDecimal costPerKg) {
        // Basic validation
        if (totalMtrs == null || totalMtrs.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Total meters cannot be null or zero.");
        }
        if (weightKg == null || weightKg.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total KGs cannot be null or negative.");
        }
        if (costPerKg == null || costPerKg.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Cost per KG cannot be null or negative.");
        }

        // Calculate total cost
        BigDecimal totalCost = weightKg.multiply(costPerKg);

        // Return cost per meter with proper rounding mode
        return totalCost.divide(totalMtrs, 2, RoundingMode.HALF_UP);
    }
}
