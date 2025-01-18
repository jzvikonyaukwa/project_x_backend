package com.axe.productTransactions.dtos;

import java.math.BigDecimal;

public interface TransactionDTO {
    Long getTransactionId();
    Long getProductId();
    String getDate();
    BigDecimal getTotalLength();
    BigDecimal getMtrsWasted();
    BigDecimal getStockOnHandLength();
    Integer getWidth();
}
