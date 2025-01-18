package com.axe.steelCoils; // Change this to your package

import com.axe.grvs.services.CalculateCostPerMtrService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(CalculateCostPerMtrService.class)    // Spring Boot test config
class CalculateCostPerMtrServiceTest {

    @Autowired
    private CalculateCostPerMtrService service;

    @Test
    @DisplayName("Should calculate cost per meter with valid inputs (happy path)")
    void testCalculateCostPerMeter_happyPath() {
        // Given
        BigDecimal weightKg   = new BigDecimal("5000");   // 5000 kg
        BigDecimal totalMtrs  = new BigDecimal("2000");   // 2000 m
        BigDecimal costPerKg  = new BigDecimal("2.50");   // $2.50/kg

        // When
        BigDecimal costPerMeter = service.calculateCostPerMeter(weightKg, totalMtrs, costPerKg);

        // Then
        // totalCost = 5000 * 2.50 = 12500
        // costPerMeter = 12500 / 2000 = 6.25 => scale=2 => 6.25
        // numeric check:
        assertNotNull(costPerMeter, "Result should not be null");
        assertEquals(new BigDecimal("6.25"), costPerMeter, "Unexpected cost-per-meter value");
        // also check scale
        assertEquals(2, costPerMeter.scale(), "Scale should be 2");
    }

    @Test
    @DisplayName("Should throw exception if totalMtrs is null")
    void testCalculateCostPerMeter_nullTotalMtrs() {
        // Given
        BigDecimal weightKg   = new BigDecimal("100");
        BigDecimal costPerKg  = new BigDecimal("5.00");

        // When
        Executable executable = () -> service.calculateCostPerMeter(weightKg, null, costPerKg);

        // Then
        IllegalArgumentException thrown =
                assertThrows(IllegalArgumentException.class, executable, "Expected exception not thrown");
        assertTrue(thrown.getMessage().contains("null or zero"), "Exception message should mention null/zero meters");
    }

    @Test
    @DisplayName("Should throw exception if totalMtrs is zero")
    void testCalculateCostPerMeter_zeroTotalMtrs() {
        // Given
        BigDecimal weightKg   = new BigDecimal("100");
        BigDecimal totalMtrs  = BigDecimal.ZERO;
        BigDecimal costPerKg  = new BigDecimal("5.00");

        // When
        Executable executable = () -> service.calculateCostPerMeter(weightKg, totalMtrs, costPerKg);

        // Then
        IllegalArgumentException thrown =
                assertThrows(IllegalArgumentException.class, executable, "Expected exception not thrown");
        assertTrue(thrown.getMessage().contains("null or zero"), "Exception message should mention null/zero meters");
    }

    @Test
    @DisplayName("Should throw exception if weightKg is null")
    void testCalculateCostPerMeter_nullWeight() {
        // Given
        BigDecimal totalMtrs = new BigDecimal("100");
        BigDecimal costPerKg = new BigDecimal("2.00");

        // When
        Executable executable = () -> service.calculateCostPerMeter(null, totalMtrs, costPerKg);

        // Then
        IllegalArgumentException thrown =
                assertThrows(IllegalArgumentException.class, executable, "Expected exception not thrown");
        assertTrue(thrown.getMessage().contains("null or negative"), "Exception message should mention null/negative KGs");
    }

    @Test
    @DisplayName("Should throw exception if weightKg is negative")
    void testCalculateCostPerMeter_negativeWeight() {
        // Given
        BigDecimal weightKg   = new BigDecimal("-10"); // negative
        BigDecimal totalMtrs  = new BigDecimal("100");
        BigDecimal costPerKg  = new BigDecimal("2.00");

        // When
        Executable executable = () -> service.calculateCostPerMeter(weightKg, totalMtrs, costPerKg);

        // Then
        IllegalArgumentException thrown =
                assertThrows(IllegalArgumentException.class, executable, "Expected exception not thrown");
        assertTrue(thrown.getMessage().contains("null or negative"), "Exception message should mention null/negative KGs");
    }

    @Test
    @DisplayName("Should throw exception if costPerKg is null")
    void testCalculateCostPerMeter_nullCost() {
        // Given
        BigDecimal weightKg   = new BigDecimal("100");
        BigDecimal totalMtrs  = new BigDecimal("50");

        // When
        Executable executable = () -> service.calculateCostPerMeter(weightKg, totalMtrs, null);

        // Then
        IllegalArgumentException thrown =
                assertThrows(IllegalArgumentException.class, executable, "Expected exception not thrown");
        assertTrue(thrown.getMessage().contains("null or negative"), "Exception message should mention null/negative cost");
    }

    @Test
    @DisplayName("Should throw exception if costPerKg is negative")
    void testCalculateCostPerMeter_negativeCost() {
        // Given
        BigDecimal weightKg   = new BigDecimal("100");
        BigDecimal totalMtrs  = new BigDecimal("50");
        BigDecimal costPerKg  = new BigDecimal("-1.00");

        // When
        Executable executable = () -> service.calculateCostPerMeter(weightKg, totalMtrs, costPerKg);

        // Then
        IllegalArgumentException thrown =
                assertThrows(IllegalArgumentException.class, executable, "Expected exception not thrown");
        assertTrue(thrown.getMessage().contains("null or negative"), "Exception message should mention null/negative cost");
    }

    @Test
    @DisplayName("Should allow costPerKg = 0 (resulting in zero cost per meter)")
    void testCalculateCostPerMeter_zeroCostAllowed() {
        // Given
        BigDecimal weightKg   = new BigDecimal("100");
        BigDecimal totalMtrs  = new BigDecimal("50");
        BigDecimal costPerKg  = BigDecimal.ZERO; // 0 is not negative, so code won't throw

        // When
        BigDecimal costPerMeter = service.calculateCostPerMeter(weightKg, totalMtrs, costPerKg);

        // Then
        // totalCost = 100 * 0 = 0
        // costPerMeter = 0 / 50 = 0 => scale=2 => 0.00
        assertNotNull(costPerMeter);
        assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), costPerMeter);
    }

    @Test
    @DisplayName("Should handle large values without issue (scale=2 result)")
    void testCalculateCostPerMeter_largeValues() {
        // Given
        BigDecimal weightKg   = new BigDecimal("1000000000"); // 1e9
        BigDecimal totalMtrs  = new BigDecimal("500000000");  // 5e8
        BigDecimal costPerKg  = new BigDecimal("3.14159");    // Pi-ish

        // When
        BigDecimal costPerMeter = service.calculateCostPerMeter(weightKg, totalMtrs, costPerKg);

        // Then
        // We won't do a fully manual math check here, but let's confirm:
        assertNotNull(costPerMeter);
        assertEquals(2, costPerMeter.scale(), "Scale should be 2 for large values as well");
        // If you want to do a rough numeric check, you could do:
        // totalCost  ~ 3.14159e9
        // totalMtrs  = 5e8
        // cost/m     ~ 3.14159e9 / 5e8 = 6.28318 => 6.28 with scale=2
        BigDecimal roughExpected = new BigDecimal("6.28");
        // Compare ignoring minor rounding differences
        assertEquals(0, costPerMeter.compareTo(roughExpected), "Cost per meter is off from rough expected");
    }
}