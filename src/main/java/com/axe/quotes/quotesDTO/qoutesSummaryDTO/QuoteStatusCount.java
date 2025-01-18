package com.axe.quotes.quotesDTO.qoutesSummaryDTO;

public interface QuoteStatusCount {
    Long getAcceptedCount();
    Long getRejectedCount();
    Long getDraftCount();
    Long getPendingApprovalCount();
}
