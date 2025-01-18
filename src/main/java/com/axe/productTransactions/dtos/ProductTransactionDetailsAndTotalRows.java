package com.axe.productTransactions.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductTransactionDetailsAndTotalRows {
    Long totalRows;
    List<ProductTransactionDTO> productTransactionDetails;
}
