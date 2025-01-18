package com.axe.stockOnHand.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface MadeProduct {

    Long getId();
    BigDecimal getLength();
    LocalDateTime getDatePicked();
    String getStatus();
    String getProductName();
    String getStick();
    String getStickType();
    String getColor();
    Float getGauge();
    Long getSteelCoilId();
//    String getSteelCoilNumber();
    LocalDateTime getManufacturedDate();
    Long getProductTransactionId();

}
