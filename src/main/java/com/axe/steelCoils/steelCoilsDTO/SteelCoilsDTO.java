package com.axe.steelCoils.steelCoilsDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SteelCoilsDTO {

    String isqGrade;
    BigDecimal width;
    String coating;
    BigDecimal gauge;

    // products
    Long steelCoilId;
    String coilNumber;
    String cardNumber;
    BigDecimal weightInKgsOnArrival;
    BigDecimal estimatedMetersRemaining;
    BigDecimal weightInKgsRemaining;
    BigDecimal estimatedMeterRunOnArrival;
    BigDecimal purchaseCostPerKg;
    BigDecimal landedCostPerMtr;
    String status;
    String finish;
    String color;

    String warehouse;
};

