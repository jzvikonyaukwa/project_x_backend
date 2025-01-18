package com.axe.consumables;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CalculateWeightedAvgPriceServiceTest {

    @Test
    public void testCalculateNewWeightedAverageRounding() {

        CalculateWeightedAvgPriceService service = new CalculateWeightedAvgPriceService();

        // given
        BigDecimal oldAveragePrice = new BigDecimal("10.50");
        BigDecimal totalQuantity = new BigDecimal("200");
        BigDecimal newPrice = new BigDecimal("15.75");
        BigDecimal newQuantity = new BigDecimal("100");

        // when - checking rounding to 4dp
        BigDecimal expected = new BigDecimal("12.2500");

        // then
        BigDecimal result = service.calculateNewWeightedAverage(oldAveragePrice, totalQuantity, newPrice, newQuantity);

        // verify
        assertEquals(expected, result, "The calculated weighted average does not match the expected value.");

    }
}