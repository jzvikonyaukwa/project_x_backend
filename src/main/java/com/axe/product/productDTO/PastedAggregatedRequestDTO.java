package com.axe.product.productDTO;


import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PastedAggregatedRequestDTO {
    String planName;
    String frameName;
    String frameType;
    String stick;
    String stickType;
    String code;
    BigDecimal length;
}
