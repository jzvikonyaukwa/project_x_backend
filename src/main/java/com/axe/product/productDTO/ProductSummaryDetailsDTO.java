package com.axe.product.productDTO;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public interface ProductSummaryDetailsDTO {

    Long getProductId();
    LocalDate getProductDateWorkBegan();
    LocalDate getProductDateLastWorkedOn();
    LocalDate getProductWorkCompleted();
    String getProductStatus();
    String getPriority();
    LocalDate getTargetDate();
    @Value("#{target.canInvoice == 1}")
    Boolean getCanInvoice();
    String getPlanName();
    String getProfile();
    Long getProfileId();
    String getClientName();
    Long getColorId();
    String getColor();
    Long getGaugeId();
    Float getGauge();
    Long getWidthId();
    Integer getWidth();
    Float getCompletedLength();
    Float getScheduledLength();
    String getQuoteStatus();
    LocalDate getQuoteDateAccepted();
}
