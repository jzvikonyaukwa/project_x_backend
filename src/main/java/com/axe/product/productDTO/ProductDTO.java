package com.axe.product.productDTO;

import com.axe.aggregated_products.AggregatedProduct;
import com.axe.colors.Color;
import com.axe.gauges.Gauge;
import com.axe.product_type.ProductType;
import com.axe.profile.Profile;
import com.axe.width.Width;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    Long id;

    String planName;
    String status;

    LocalDateTime dateWorkBegan;
    LocalDateTime dateWorkCompleted;
    LocalDateTime lastWorkedOn;
    LocalDate targetDate;
    String priority;

    Gauge gauge;
    Color color;
    Width width;
    Profile profile;
    Boolean canInvoice;

    ProductType productType;

    List<AggregatedProduct> aggregatedProducts;
    Long invoiceId;
    String finish;
    String notes;

    String frameType;
    String frameName;
    BigDecimal totalLength;
    Integer totalQuantity;

    BigDecimal costPrice;
    BigDecimal sellPrice;
}
