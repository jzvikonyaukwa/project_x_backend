package com.axe.saleOrder.models;

import java.math.BigDecimal;

public interface ProductsTotalLengthOnOrder {

    BigDecimal getWidth();
    BigDecimal getGauge();
    String getColor();
    BigDecimal getTotalLengthReserved();
    BigDecimal getTotalLengthSoldNotReserved();
    Integer getCuttingListCount();
}
