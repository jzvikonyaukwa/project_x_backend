package com.axe.product.productDTO;

import com.axe.colors.Color;
import com.axe.gauges.Gauge;
import com.axe.product_type.ProductType;
import com.axe.profile.Profile;
import com.axe.width.Width;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductDTO {
    Long id;
    LocalDate targetDate;
    Gauge gauge;
    Color color;
    Width width;
    Profile profile;
    String code;
    ProductType productType;
    String frameType;
    String frameName;
    List<ProductLengthQuantityDTO> aggregatedProducts;
    BigDecimal totalLength;
    Integer totalQuantity;
    BigDecimal costPrice;
    BigDecimal sellPrice;
}
