package com.axe.productTransactions.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
public class ProductTransactionDTO {
    private Long id;
    private LocalDateTime date;
    private Long steelCoilId;
    private String coilNumber;
    private Long manufacturedProductId;
    private Long productId;
    private String manufacturedProductCode;
    private String frameName;
    private BigDecimal length;
    private Long wastageId;
    private BigDecimal mtrsWasted;
    private Long stockOnHandId;
    private BigDecimal stockOnHandLength;

    public ProductTransactionDTO(Long id, LocalDateTime date, Long steelCoilId,
                                 String coilNumber, Long manufacturedProductId, Long productId,
                                String manufacturedProductCode, String frameName, BigDecimal length,
                                 Long wastageId, BigDecimal mtrsWasted, Long stockOnHandId,
                                 BigDecimal stockOnHandLength) {
        this.id = id;
        this.date = date;
        this.steelCoilId = steelCoilId;
        this.coilNumber = coilNumber;
        this.manufacturedProductId = manufacturedProductId;
        this.productId = productId;
        this.manufacturedProductCode = manufacturedProductCode;
        this.frameName = frameName;
        this.length = length;
        this.wastageId = wastageId;
        this.mtrsWasted = mtrsWasted;
        this.stockOnHandId = stockOnHandId;
        this.stockOnHandLength = stockOnHandLength;
    }
}
