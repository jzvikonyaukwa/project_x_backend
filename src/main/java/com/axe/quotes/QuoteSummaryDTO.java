package com.axe.quotes;

import com.axe.common.enums.QuoteStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuoteSummaryDTO {

    @JsonProperty("statusCounts")
    private Map<QuoteStatus, Long> statusCounts;

    @JsonProperty("statusTotalValues")
    private Map<QuoteStatus, Double> statusTotalValues;

    private String date;
}