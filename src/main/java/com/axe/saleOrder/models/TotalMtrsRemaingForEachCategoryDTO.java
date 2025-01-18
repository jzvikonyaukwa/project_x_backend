package com.axe.saleOrder.models;

import java.math.BigDecimal;

public interface TotalMtrsRemaingForEachCategoryDTO {
    BigDecimal getWidth();
    BigDecimal getGauge();
    String getColor();
    BigDecimal getTotalMtrsRemaining();
}
