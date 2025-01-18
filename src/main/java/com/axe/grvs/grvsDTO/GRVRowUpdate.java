package com.axe.grvs.grvsDTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class GRVRowUpdate {
    BigDecimal estimatedMeterRunOnArrival;
    BigDecimal estimatedMetersRemaining;
    Float purchaseCostPerKg;
    String dateReceived;
    String supplierGrvCode;
    Long supplierId;
    String warehouse;
    BigDecimal weightInKgsOnArrival;
    String coilNumber;
    String cardNumber;
    BigDecimal landedCostPerMtr;
    String grvComments;
    String supplierName;
    Long steelCoilId;
    String productStatus;
    String isqGrade;
    BigDecimal width;
    String color;
    Float gauge;
    Long grvId;
    String finish;
    String coating;
    BigDecimal grvTotal;
    String consumableName;

}