package com.axe.grvs.grvsDTO.summaryDTO;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class MonthlySummaryDTO {
    private int month;
    private long steelCoilsCount;
    private long consumablesCount;
}
