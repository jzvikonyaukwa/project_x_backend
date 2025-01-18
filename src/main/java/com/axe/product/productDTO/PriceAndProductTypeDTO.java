package com.axe.product.productDTO;

import com.axe.gauges.Gauge;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
public class PriceAndProductTypeDTO {
    private Long cuttingListId;
    private Long productTypeId;
    private BigDecimal pricePerMeter;
    private Gauge gauge;
    private LocalDateTime datePriceUpdated;
    private LocalDateTime datePriceSet;

}
