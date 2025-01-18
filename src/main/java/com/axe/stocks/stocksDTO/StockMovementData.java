package com.axe.stocks.stocksDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface StockMovementData {
    String getCoilNumber();
    String getCardNumber();
    BigDecimal getMeters();
    BigDecimal getWeight();
    BigDecimal getLandedCost();
    String getSupplier();
    LocalDate getConsignmentTransferDate();
    LocalDate getGrvDate();
    Long getGrvId();
    Long getInterBranchTransferId();
    String getWarehouse();
    String getCardNumberFrom();
    String getCoilNumberFrom();

}
