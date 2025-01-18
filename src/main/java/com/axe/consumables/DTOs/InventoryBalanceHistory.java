package com.axe.consumables.DTOs;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface InventoryBalanceHistory {

    LocalDate getTransactionDate();

    BigDecimal getStockBalance();

    BigDecimal getTransactionQuantity();

    BigDecimal getTransactionValue();

    BigDecimal getUnitCost();

    String getTransactionType();

    Integer getReferenceID();

    // String getItemName();
}