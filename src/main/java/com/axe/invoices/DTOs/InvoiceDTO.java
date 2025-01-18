package com.axe.invoices.DTOs;

import java.time.LocalDate;

import lombok.Value;

@Value
public class InvoiceDTO {
  private Long id;

  private LocalDate dateInvoiced;

  private Boolean paid;

  private Long quoteId;

  private String clientName;
}