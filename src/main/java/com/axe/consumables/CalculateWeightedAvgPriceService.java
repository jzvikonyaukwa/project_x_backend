package com.axe.consumables;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CalculateWeightedAvgPriceService {

    public BigDecimal calculateNewWeightedAverage(BigDecimal oldAveragePrice, BigDecimal totalQuantity,
                                                  BigDecimal newPrice, BigDecimal newQuantity) {

        if(oldAveragePrice == null){
            return newPrice;
        }
        BigDecimal totalOldCost = oldAveragePrice.multiply(totalQuantity);
        BigDecimal totalNewCost = newPrice.multiply(newQuantity);

        BigDecimal totalCost = totalOldCost.add(totalNewCost);
        BigDecimal newTotalQuantity = totalQuantity.add(newQuantity);

        return totalCost.divide(newTotalQuantity, 4, RoundingMode.HALF_UP);
    }
}
