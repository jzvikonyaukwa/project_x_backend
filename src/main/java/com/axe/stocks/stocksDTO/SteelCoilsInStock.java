package com.axe.stocks.stocksDTO;

import java.math.BigDecimal;

public interface SteelCoilsInStock {

        Long getSteelCoilId();
        String getAxeCoilId();
        String getCardNumber();
        BigDecimal getEstMtrsRemaining();
        BigDecimal getLandedCostPerMtr();
        String getISQGrade();
        String getCoating();
        Float getGauge();
        String getColor();
        Float getWidth();
        String getWarehouse();
        String getStatus();
}
