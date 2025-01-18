package com.axe.grvs.grvsDTO;

import com.axe.colors.Color;
import com.axe.consignor.Consignor;
import com.axe.gauges.Gauge;
import com.axe.warehouse.Warehouse;
import com.axe.width.Width;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class SteelCoilPostDTO {
    Long productOnPurchaseOrderId;
    Long steelCoilId;
    String coilNumber;
    String cardNumber;
    String status;
    String isqGrade;
    String coating;
    BigDecimal weightInKgsOnArrival;
    BigDecimal estimatedMeterRunOnArrival;
    BigDecimal estimatedMetersRemaining;
    BigDecimal purchaseCostPerKg;
    BigDecimal landedCostPerKg;
    BigDecimal landedCostPerMtr;
    Color color;
    Width width;
    Gauge gauge;
    Warehouse warehouse;
    BigDecimal weightOrdered;
    Consignor consignor;

}
