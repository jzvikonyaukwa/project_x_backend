package com.axe.product.productDTO;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductLengthQuantityDTO {
    Long quantity;
    BigDecimal length;
}
