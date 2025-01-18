package com.axe.productTransactions.dtos;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@SqlResultSetMapping(
        name = "ProductTransactionDetailsMapping",
        classes = @ConstructorResult(
                targetClass = ProductTransactionDetailsImpl.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "date", type = LocalDate.class),
//                        @ColumnResult(name = "productId", type = Long.class),
                        @ColumnResult(name = "length", type = BigDecimal.class),
                        @ColumnResult(name = "productId", type = Long.class),
                        @ColumnResult(name = "manufacturedProductCode", type = String.class),
                        @ColumnResult(name = "frameName", type = String.class),
                        @ColumnResult(name = "steelCoilId", type = Long.class),
                        @ColumnResult(name = "coilNumber", type = String.class),
                        @ColumnResult(name = "wastageId", type = Long.class),
                        @ColumnResult(name = "mtrsWasted", type = BigDecimal.class),
                        @ColumnResult(name = "stockOnHandId", type = Long.class),
                        @ColumnResult(name = "stockOnHandLength", type = BigDecimal.class),
                        @ColumnResult(name = "steelCoilWidth", type = Double.class),
                        @ColumnResult(name = "gauge", type = Double.class),
                        @ColumnResult(name = "color", type = String.class),
                        @ColumnResult(name = "productType", type = String.class)

                }
        )
)

@NoArgsConstructor

public class ProductTransactionDetailsImpl implements ProductTransactionDetails {
    @Id
    private Long id;
    private LocalDate date;
    private Long manufacturedProductId;
    private BigDecimal length;
    private Long productId;
    private String manufacturedProductCode;
    private String frameName;
    private Long steelCoilId;
    private String coilNumber;
    private Long wastageId;
    private BigDecimal mtrsWasted;
    private Long stockOnHandId;
    private BigDecimal stockOnHandLength;

    private Double steelCoilWidth;
    private Double gauge;
    private String color;
    private String  productType;







    public ProductTransactionDetailsImpl(Long id, LocalDate date, Long manufacturedProductId, BigDecimal length, Long productId,
                                         String manufacturedProductCode, String frameName, Long steelCoilId, String coilNumber,
                                         Long wastageId, BigDecimal mtrsWasted, Long stockOnHandId, BigDecimal stockOnHandLength,
                                         Double steelCoilWidth,Double gauge,String color,String  productType

    ) {
        this.id = id;
        this.date = date;
        this.manufacturedProductId = manufacturedProductId;
        this.length = length;
        this.productId = productId;
        this.manufacturedProductCode = manufacturedProductCode;
        this.frameName = frameName;
        this.steelCoilId = steelCoilId;
        this.coilNumber = coilNumber;
        this.wastageId = wastageId;
        this.mtrsWasted = mtrsWasted;
        this.stockOnHandId = stockOnHandId;
        this.stockOnHandLength = stockOnHandLength;

        this.steelCoilWidth = steelCoilWidth;
        this.gauge = gauge;
        this.color = color;
        this.productType = productType;
    }

    // Implement getters
    @Override
    public Long getId() { return id; }
    @Override
    public LocalDate getDate() { return date; }
    @Override
    public Long getManufacturedProductId() { return manufacturedProductId; }
    @Override
    public BigDecimal getLength() { return length; }
    @Override
    public Long getProductId() { return productId; }
    @Override
    public String getManufacturedProductCode() { return manufacturedProductCode; }
    @Override
    public String getFrameName() { return frameName; }
    @Override
    public Long getSteelCoilId() { return steelCoilId; }
    @Override
    public String getCoilNumber() { return coilNumber; }
    @Override
    public Long getWastageId() { return wastageId; }
    @Override
    public BigDecimal getMtrsWasted() { return mtrsWasted; }
    @Override
    public Long getStockOnHandId() { return stockOnHandId; }
    @Override
    public BigDecimal getStockOnHandLength() { return stockOnHandLength; }

    @Override
    public Double getSteelCoilWidth() { return steelCoilWidth; }
    @Override
    public Double getGauge() { return gauge; }
    @Override
    public String getColor() { return color; }
    @Override
    public String getProductType() { return productType; }
}
