package com.axe.productTransactions;

import com.axe.productTransactions.dtos.ProductTransactionDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductTransactionsCustomRepository {
    Page<ProductTransactionDetails> getFilteredProductTransactionDetails(Pageable pageable, String filters);
}
