package com.axe.projects.projectDTO.project_overviewDTO;

import com.axe.consumablesOnQuote.ConsumableOnQuote;
import com.axe.product.Product;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record QuoteDTO (
     Long id,
     String status,
     LocalDate dateIssued,
     LocalDate dateAccepted,
     List<Product> products,
    List<ConsumableOnQuote> consumableOnQuotes
){}
