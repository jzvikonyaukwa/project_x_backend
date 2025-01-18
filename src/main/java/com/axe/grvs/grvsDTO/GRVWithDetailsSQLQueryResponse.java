package com.axe.grvs.grvsDTO;

import com.axe.grvs.grvsDTO.summaryDTO.GRVGroupedData;

import java.util.List;

public record GRVWithDetailsSQLQueryResponse (List<GRVGroupedData> grvDetails, long totalElements){
}
