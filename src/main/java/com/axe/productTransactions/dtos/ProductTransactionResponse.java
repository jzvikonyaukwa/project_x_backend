package com.axe.productTransactions.dtos;

import com.axe.productTransactions.ProductTransaction;
import lombok.Builder;

import java.util.List;


@Builder
public record ProductTransactionResponse(List<ProductTransaction> productTransactions, long totalElements) {
}
