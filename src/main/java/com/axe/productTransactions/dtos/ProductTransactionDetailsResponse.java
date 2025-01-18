package com.axe.productTransactions.dtos;

import lombok.Builder;

import java.util.List;

@Builder
public record ProductTransactionDetailsResponse(List<ProductTransactionDetails> productTransactionDetails, long totalElements) {

}
