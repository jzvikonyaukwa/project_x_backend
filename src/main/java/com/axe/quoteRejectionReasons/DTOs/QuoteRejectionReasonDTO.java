package com.axe.quoteRejectionReasons.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuoteRejectionReasonDTO {
    private Long id;
    private String reason;
}
