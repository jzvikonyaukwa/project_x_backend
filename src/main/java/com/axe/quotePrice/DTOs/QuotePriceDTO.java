package com.axe.quotePrice.DTOs;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class QuotePriceDTO {
    private Long id;
 
    private String priceType;
 
    private BigDecimal markUp;
 
    private LocalDate dateEdited;
}
