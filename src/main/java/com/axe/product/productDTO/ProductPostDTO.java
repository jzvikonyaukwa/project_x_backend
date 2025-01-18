package com.axe.product.productDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductPostDTO {
    private Long productId;
    private Long coilId;
    private String dateManufactured;
}

