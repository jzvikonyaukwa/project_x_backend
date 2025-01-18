package com.axe.grvs.grvsDTO.summaryDTO;

import com.axe.grvs.grvsDTO.GRVWithDetailsSQLQuery;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GRVGroupedData {
    private Long grvId;
    private String dateReceived;
    private String grvComments;
    private String supplierGrvCode;
    private String supplierName;
    private List<GRVWithDetailsSQLQuery> details;
}
