package com.axe.quotes.quotesDTO.qoutesSummaryDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class QuotesPerDayDTO {
    private String day;
    private Long quotesCount;
    private LocalDate date;
}