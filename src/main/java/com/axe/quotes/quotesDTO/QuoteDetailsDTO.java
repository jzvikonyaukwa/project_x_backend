package com.axe.quotes.quotesDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuoteDetailsDTO {
    private Long id;
    private String clientName;
    private String projectName;
    private LocalDate dateIssued;
    private LocalDate dateLastModified;
    private LocalDate dateAccepted;
    private String status;
    private String notes;
    private Long clientId;
}
