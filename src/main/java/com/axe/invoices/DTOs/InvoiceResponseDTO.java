package com.axe.invoices.DTOs;

import java.time.LocalDate;

public interface InvoiceResponseDTO {
    Long getId();
    LocalDate getDateInvoiced();
    Boolean getPaid();
    Long getQuoteId();
    String getName();
}
