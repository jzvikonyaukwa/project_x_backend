package com.axe.quotes.quotesDTO.qoutesSummaryDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public  class QuoteSummaryItemDTO {
    private String planName;
    private BigDecimal count;
    public QuoteSummaryItemDTO(String planName, Long count) {
        this.planName = planName;
        this.count = BigDecimal.valueOf(count);
    }

}
