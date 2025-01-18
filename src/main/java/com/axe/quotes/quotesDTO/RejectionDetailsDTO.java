package com.axe.quotes.quotesDTO;

import java.time.LocalDate;

import com.axe.quoteRejectionReasons.DTOs.QuoteRejectionReasonDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RejectionDetailsDTO {
    private LocalDate date;
    private QuoteRejectionReasonDTO rejectionReason;
}
