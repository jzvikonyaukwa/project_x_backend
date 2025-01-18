package com.axe.stocks.stocksDTO;


import java.math.BigDecimal;
import java.time.LocalDate;

public interface StocksDTO {

    Long getProductTypeId();
    String getColor();
    String getISQGrade();
    Float getWidth();
    String getCoating();
    String getWarehouse();
    String getStatus();
    Float getGauge();
    Long getSteelCoilId();
    String getCoilNumber();
    String getCardNumber();
    Float getWeightOnArrival();
    Float getMeterRemaining();
    BigDecimal getLandedCostPerMtr();
    Float getEstMtrsOnArrival();
    String getSupplier();
    LocalDate getPurchaseOrderIssued();
    LocalDate getDateReceived();
    String getConsignor();

}
