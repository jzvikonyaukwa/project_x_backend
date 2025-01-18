package com.axe.productTransactions;

import com.axe.productTransactions.dtos.ProductTransactionDetailsResponse;
import com.axe.productTransactions.dtos.ProductTransactionResponse;
import com.axe.productTransactions.dtos.TransactionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-transactions")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class ProductTransactionsController {

    private static final Logger log = LoggerFactory.getLogger(ProductTransactionsController.class);
    private final ProductTransactionsService productTransactionsService;

    public ProductTransactionsController(ProductTransactionsService productTransactionsService) {
        this.productTransactionsService = productTransactionsService;
    }

    @GetMapping
    public ResponseEntity<ProductTransactionResponse> getProductTransactionWithPaging(@RequestParam(defaultValue = "0") Integer pageNo,
                                                                                    @RequestParam(defaultValue = "10") Integer pageSize){
        ProductTransactionResponse response = productTransactionsService.getProductTransactionByPagination(pageNo, pageSize);

        return ResponseEntity.ok(response);

    }
    @GetMapping("details")
    public ResponseEntity<ProductTransactionDetailsResponse> getAllProductTransactionDetails(@RequestParam(defaultValue = "0") Integer pageNo,
                                                                                             @RequestParam(defaultValue = "10") Integer pageSize,
                                                                                             @RequestParam(required = false) String filters) {
        return ResponseEntity.ok(productTransactionsService.getAllProductTransactionDetailsByPagination(pageNo, pageSize,filters));
    }


    @GetMapping("width/{width}")
    public ResponseEntity<List<TransactionDTO>> getProductTransactionsForThisWidth(@PathVariable int width){
        return ResponseEntity.ok(productTransactionsService.getProductTransactionsForThisWidth(width));
    }


}
