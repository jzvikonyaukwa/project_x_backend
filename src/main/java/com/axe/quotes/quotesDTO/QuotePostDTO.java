package com.axe.quotes.quotesDTO;

import com.axe.quotePrice.QuotePrice;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class QuotePostDTO {

    LocalDate dateIssued;
    String status;
    Boolean stdPricing;
    String notes;
    Long clientId;
    Long projectId;
//    Long productTypeId;
    QuotePrice quotePrice;

}
