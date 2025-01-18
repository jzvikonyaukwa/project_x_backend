package com.axe.product.productDTO;

import java.time.LocalDateTime;

public interface ProductSummaryDTO {
    Long getId();
    String getFinish();
    String getColor();
    String getProfile();
    Float getGauge();
    String getCuttingListStatus();
    Float getKgsToMeters();
    Long getManufacturingProductId();
    LocalDateTime getDateWorkBegan();
    Long getMachineId();
    String getMachineName();
    Long getQuoteId();
    LocalDateTime getDateQuoteAccepted();
    String getClientName();

}
