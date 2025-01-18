package com.axe.product_type.productTypeDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductTypeDTO {
    private Long id;
    private String name;
    private String code;
    private String categoryName;
}
