package com.axe.steelCoils.steelCoilsDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface SteelCoilDetailsDTO {

    Long getSteelCoilId();
    String getCoilNumber();
    String getCardNumber();
    BigDecimal getEstMtrsRemaining();
    BigDecimal getEstMtrsOnArrival();
    BigDecimal getWeightInKgsOnArrival();
    Float getLandedCostPerMtr();
    String getColor();
    String getCssColor();
    String getFinish();
    String getIsqGrade();
    String getCoating();
    Float getWidth();
    Float getGauge();
    String getSupplierName();
    LocalDate getDateReceived();
    String getConsignor();
    String getWarehouse();
    Long getGrvId();

}
