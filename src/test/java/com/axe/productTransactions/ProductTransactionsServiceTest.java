package com.axe.productTransactions;

import com.axe.productTransactions.dtos.ProductTransactionDetails;
import com.axe.productTransactions.dtos.ProductTransactionDetailsImpl;
import com.axe.productTransactions.dtos.ProductTransactionDetailsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ProductTransactionsServiceTest {

//    @Mock
//    private ProductTransactionsRepository productTransactionsRepository;
//
//    @InjectMocks
//    private ProductTransactionsService productTransactionsService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGetAllProductTransactionDetailsByPagination() {
//        int pageNo = 0;
//        int pageSize = 10;
//        String filters = "id:number:equals:19199,date:date:equals:undefined,steelCoilId:number:equals:400";
//
//        ProductTransactionDetails detail1 = new ProductTransactionDetailsImpl(
//                19199L, null, null, null, null, null, null, null, null, null, null, null, null);
//        ProductTransactionDetails detail2 = new ProductTransactionDetailsImpl(
//                19200L, null, null, null, null, null, null, null, null, null, null, null, null);
//
//        Page<ProductTransactionDetails> page = new PageImpl<>(Arrays.asList(detail1, detail2));
//
//        when(productTransactionsRepository.getFilteredProductTransactionDetails(any(Pageable.class), any(String.class)))
//                .thenReturn(page);
//
//        ProductTransactionDetailsResponse response = productTransactionsService.getAllProductTransactionDetailsByPagination(pageNo, pageSize, filters);
//
//        assertEquals(2, response.productTransactionDetails().size());
//        assertEquals(2, response.totalElements());
//    }
}
