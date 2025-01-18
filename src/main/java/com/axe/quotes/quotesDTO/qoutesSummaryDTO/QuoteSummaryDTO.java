package com.axe.quotes.quotesDTO.qoutesSummaryDTO;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class QuoteSummaryDTO {
    private LocalDate startDate;
    private LocalDate endDate;
    private List<QuoteSummaryItemDTO> items;



}
