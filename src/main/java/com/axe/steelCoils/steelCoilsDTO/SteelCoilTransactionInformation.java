package com.axe.steelCoils.steelCoilsDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface SteelCoilTransactionInformation {

    Long getProductTransactionId();
    String getClient();
    Long getQuoteId();
    Long getManufacturedProductId();
    BigDecimal getManufacturedProductLength();
    Long getStockOnHandId();
    BigDecimal getStockOnHandLength();
    Long getWastageId();
    BigDecimal getWastageLength();
    LocalDate getDate();
    String getCode();
    String getFrameType();
    String getFrameName();
    String getPlanName();
}
